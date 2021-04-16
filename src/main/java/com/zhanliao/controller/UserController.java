package com.zhanliao.controller;

import com.alibaba.druid.util.StringUtils;
import com.zhanliao.controller.viewObject.UserVO;
import com.zhanliao.dataobject.UserPasswordDO;
import com.zhanliao.erro.BusinessException;
import com.zhanliao.erro.EmBusinessError;
import com.zhanliao.response.CommonReturnType;
import com.zhanliao.service.UserService;
import com.zhanliao.service.model.UserModel;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


@Controller("user")
@RequestMapping("/user")
//@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
//@CrossOrigin(allowCredentials = "true", origins = "http://127.0.0.1:8848")
@CrossOrigin(originPatterns = "*",allowCredentials="true")    // 2.24版本以上是不能在设定allowCredentials = "true“时候，设定allowedHeaders = "*"，将解决跨域访问的问题
//@CrossOrigin
public class UserController extends BaseController{

    @Autowired
    UserService userService;

    @Autowired
    HttpServletRequest httpServletRequest;

    @Autowired
    private RedisTemplate redisTemplate;

    //用户登录
    @RequestMapping(value = "/login", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED}) //后面这个参数需要和前端对应
    @ResponseBody
    public CommonReturnType login(@RequestParam(name = "telephone")String telephone,
                                     @RequestParam(name = "password")String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        // 入参校验
        if(org.apache.commons.lang3.StringUtils.isEmpty(telephone) || StringUtils.isEmpty(password)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        // 用户登录服务，用来校验用户登录是否合法
        UserModel userModel = userService.validateLogin(telephone, this.EncodeByMd5(password));

        // 将登录凭证加入到用户登录成功的session内【后面的课程会改成分布式session】
        // 如果用户session有IS_LOGIN这个标志就设为true
        // 登录成功之后将成功信息放到session
        // 给前端返回一个信息
        /*
        【登陆成功之后将对应的登录信息和登录凭证一起存入Redis中】
        1.生成登录token，UUID
        2.建立用户与token的映射
         */
        String uuidToken = UUID.randomUUID().toString();
        uuidToken = uuidToken.replace("-", "");
        redisTemplate.opsForValue().set(uuidToken, userModel);
        redisTemplate.expire(uuidToken, 1, TimeUnit.HOURS);

        /*this.httpServletRequest.getSession().setAttribute("IS_LOGIN", true);
        this.httpServletRequest.getSession().setAttribute("LOGIN_USER", userModel);*/

        //
        return CommonReturnType.create(uuidToken);
    }

    // 用户注册接口
    @RequestMapping(value = "/register", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED}) //后面这个参数需要和前端对应,
    @ResponseBody
    public CommonReturnType register(@RequestParam(name = "telephone") String telephone,
                                     @RequestParam(name = "otpCode") String otpCode,
                                     @RequestParam(name = "name") String name,
                                     @RequestParam(name = "gender") Integer gender,
                                     @RequestParam(name = "age") Integer age,
                                     @RequestParam(name = "password") String password
                                     ) throws BusinessException, NoSuchAlgorithmException, UnsupportedEncodingException {
        // 验证手机号与对应得OTPCode相符合
        /*String inSessionOTPCode = (String) this.httpServletRequest.getSession().getAttribute("telephone");
        if(!com.alibaba.druid.util.StringUtils.equals(otpCode, inSessionOTPCode)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "短信验证码不符合");
        }*/
        // 用户的注册流程
        UserModel userModel = new UserModel();
        userModel.setTelephone(telephone);
        userModel.setName(name);
        userModel.setAge(age);
        userModel.setGender(gender);
        userModel.setRegisterMode("byphone");
        // 加密
//        userModel.setEncryptPassword(MD5Encoder.encode(password.getBytes())password); // java自带的MD5加密方式，只支持16位的加密方式
        String strByMD5 = this.EncodeByMd5(password);
        userModel.setEncryptPassword(strByMD5);
        userService.register(userModel);
        return CommonReturnType.create(null);
    }

    public String EncodeByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        // 确定加密计算方式
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder = new BASE64Encoder();
        // 加密字符串
        String newStr = base64Encoder.encode(md5.digest(str.getBytes("utf-8")));
        return newStr;
    }


    @RequestMapping(value = "/getotp", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})//
    @ResponseBody
    public CommonReturnType getOTP(@RequestParam(name = "telephone")String telephone){
        // 1.需要按照一定规则生成OTP验证码
        Random random = new Random();
        int randomInt = random.nextInt(89999);
        randomInt += 10000;
        String otpCode = String.valueOf(randomInt);

        // 2.将验证码与对应用户的手机号关联，使用httpSession的方式绑定他的手机号和验证码
        this.httpServletRequest.getSession().setAttribute(telephone, otpCode);

        // 3. 将OTP验证码通过短信通道发送给用户，此处打印到控制台（实际生产是保密的）
        System.out.println("telephone = " + telephone + "   &   OPTCode = " + otpCode);
        return CommonReturnType.create(null);
    }


    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam(name = "id") Integer id) throws BusinessException {
        // 调用service服务获取对应id的用户对象并返回给前端
        UserModel userModel = userService.getUserById(id);
        // 若获取的对应用户信息不存
        if(userModel == null){
//            userModel.setEncryptPassword("2312");
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }

        // 讲核心领域模型用户对象转化为可供UI使用的viewobject
        UserVO userVO = convertFromModel(userModel);
        CommonReturnType commonReturnType = new CommonReturnType();
        return commonReturnType.create(userVO);
    }
    private UserVO convertFromModel(UserModel userModel){
        if(userModel == null){
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel, userVO);
        return userVO;
    }



}
