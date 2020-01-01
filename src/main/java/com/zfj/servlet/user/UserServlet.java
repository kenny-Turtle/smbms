package com.zfj.servlet.user;

import com.alibaba.fastjson.JSONArray;
import com.mysql.cj.util.StringUtils;
import com.mysql.cj.xdevapi.JsonArray;
import com.zfj.pojo.Role;
import com.zfj.pojo.User;
import com.zfj.service.role.RoleServiceImpl;
import com.zfj.service.user.UserService;
import com.zfj.service.user.UserServiceImpl;
import com.zfj.util.Constants;
import com.zfj.util.PageSupport;
import sun.security.util.Password;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author zfj
 * @create 2019/12/22 19:04
 */
public class UserServlet extends HttpServlet {

    private UserService userService;
    public UserServlet(){
        userService=new UserServiceImpl();
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if(method.equals("savepwd")&&method!=null){
            this.updatePwd(req,resp);
        }else if (method.equals("pwdmodify")&&method!=null){
            this.pwdModify(req,resp);
        }else if(method.equals("query")&&method!=null){
            this.query(req,resp);
        }else if(method.equals("add")&&method!=null){
            this.add(req,resp);
        }else if(method.equals("deluser")&&method!=null){
            this.delUser(req,resp);
        }


    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    //删除用户
    public void delUser(HttpServletRequest req,HttpServletResponse resp) throws ServletException,IOException{
        String id = req.getParameter("uid");
        int delId=0;
        try {
            delId=Integer.parseInt(id);
        } catch (Exception e) {
            delId=0;
        }
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if(delId<=0){
            resultMap.put("delResult","notexist");
        }
    }

    //添加用户
    public void add(HttpServletRequest req,HttpServletResponse resp) throws ServletException,IOException{
        System.out.println("add()---------------");
        String userCode = req.getParameter("userCode");
        String userName = req.getParameter("userName");
        String userPassword = req.getParameter("userPassword");
        String gender = req.getParameter("gender");
        String birthday = req.getParameter("birthday");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String userRole = req.getParameter("userRole");

        User user = new User();
        user.setUserCode(userCode);
        user.setUserName(userName);
        user.setUserPassword(userPassword);
        user.setAddress(address);
        try {
            user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        user.setGender(Integer.valueOf(gender));
        user.setPhone(phone);
        user.setUserRole(Integer.valueOf(userRole));
        user.setCreationDate(new Date());
        user.setCreatedBy(((User)req.getSession().getAttribute(Constants.USER_SESSION)).getId());

        if(userService.add(user)){
            resp.sendRedirect(req.getContextPath()+"/jsp/user.do?method=query");
        }else{
            req.getRequestDispatcher("useradd.jsp").forward(req,resp);
        }
    }
    //》》》》》》》》》重点，难点《《《《《《《《《《《
    //根据条件查询用户列表
    public void query(HttpServletRequest req,HttpServletResponse resp){

        //查询用户列表

        //从前端获取数据
        String queryUserName = req.getParameter("queryname");
        String temp = req.getParameter("queryUserRole");
        String pageIndex = req.getParameter("pageIndex");
        int queryUserRole=0;

        List<User> userList=null;

        //获取用户列表
//        userService.getUserList(queryUserName,queryUserRole,pageIndex,);


        //第一次走这个请求，一定是走第一页，页面大小固定
        int pageSize=Constants.PAGE_SIZW;//可以把这个写到配置文件中，方便后期修改。
        int currentPageNo=1;

        System.out.println("queryUserName servlet--------"+queryUserName);
        System.out.println("queryUserRole servlet--------"+queryUserRole);
        System.out.println("query pageIndex--------- > " + pageIndex);

        if(queryUserName==null){
            queryUserName="";
        }
        if(temp!=null&&!temp.equals("")){
            queryUserRole=Integer.parseInt(temp);  //给查询赋值
        }if(pageIndex!=null){
            currentPageNo = Integer.parseInt(pageIndex);
        }

        //获取用户的总数(分页：上一页，下一页）
        int totalCount = userService.getUserCount(queryUserName, queryUserRole);
        //总页数支持
        PageSupport pageSupport = new PageSupport();
        pageSupport.setCurrentPageNo(currentPageNo);
        pageSupport.setPageSize(pageSize);
        pageSupport.setTotalCount(totalCount);

//        int totalPageCount = pageSupport.getTotalCount();
        int totalPageCount=((int)(totalCount/pageSize))+1;

        //控制首页和尾页
        //如果特面小于1，就显示第一页的东西.如果页面大于最后一页，就使当前页为最大页
        if(currentPageNo<1){
            currentPageNo=1;
        }else if(currentPageNo>totalPageCount){
            currentPageNo=totalPageCount;
        }


        //获取用户列表展示
        userList=userService.getUserList(queryUserName,queryUserRole,currentPageNo,pageSize);
        req.setAttribute("userList",userList);
        RoleServiceImpl roleService = new RoleServiceImpl();
        List<Role> roleList = roleService.getRoleList();
        req.setAttribute("roleList",roleList);
        req.setAttribute("totalCount",totalCount);
        req.setAttribute("currentPageNo",currentPageNo);
        req.setAttribute("totalPageCount",totalPageCount);
        req.setAttribute("queryUserName",queryUserName);
        req.setAttribute("queryUserRole",queryUserRole);

        //返回前端
        try {
            req.getRequestDispatcher("userlist.jsp").forward(req,resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //修改密码
    public void updatePwd(HttpServletRequest req, HttpServletResponse resp){

        //从session里面拿id
        User user = (User) req.getSession().getAttribute(Constants.USER_SESSION);
        Integer id = user.getId();
        String newpassword = req.getParameter("newpassword");
        System.out.println(newpassword);
        boolean flag=false;
        if(user!=null&& !StringUtils.isNullOrEmpty(newpassword)){
            flag = userService.updatePwd(id, newpassword);

            if(flag){
                req.setAttribute("message","修改密码成功,请退出使用新密码登录");
                //密码修改成功，移除当前session
                req.getSession().removeAttribute(Constants.USER_SESSION);
            }else{
                req.setAttribute("message","密码修改失败");

            }
        }else{
            req.setAttribute("message","新密码有问题");
        }

        try {
            req.getRequestDispatcher("pwdmodify.jsp").forward(req,resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //验证旧密码,旧密码在session中
    public void pwdModify(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Object o = request.getSession().getAttribute(Constants.USER_SESSION);
        String oldpassword = request.getParameter("oldpassword");
        Map<String, String> resultMap = new HashMap<String, String>();

        if(null == o ){//session过期
            resultMap.put("result", "sessionerror");
        }else if(StringUtils.isNullOrEmpty(oldpassword)){//旧密码输入为空
            resultMap.put("result", "error");
        }else{
            String sessionPwd = ((User)o).getUserPassword();
            if(oldpassword.equals(sessionPwd)){
                resultMap.put("result", "true");
            }else{//旧密码输入不正确
                resultMap.put("result", "false");
            }
        }
//        System.out.println(resultMap);
        System.out.println(JSONArray.toJSON(resultMap));
        System.out.println(JSONArray.toJSONString(resultMap));

        response.setContentType("application/json");
        PrintWriter outPrintWriter = response.getWriter();
        outPrintWriter.write(JSONArray.toJSONString(resultMap));
        outPrintWriter.flush();
        outPrintWriter.close();


    }
}
