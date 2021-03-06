package ea.view.action;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.cli.Digest;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import ea.base.BaseAction;
import ea.domain.Photo;
import ea.domain.User;


@Controller
@Scope("prototype")
public class UserAction extends BaseAction<User>{

	private static final long serialVersionUID = 1L;
	// 对应js里面的fileObjName，而不是input type ="text" 里面的name值,
	//这里也可以不用list，用File uploadify。因为虽然是批量上传，但还是一个一个的处理文件的
	//等一个文件处理完后才会处理第二个
	private File uploadify; 
	private String uploadifyFileName;
	private String uploadifyContentType;
	private static final String savePath = "imgs";// 文件上传后保存的路径
	private String mvUrl;
	private Long userid;//标记哪个用户上传
	
	
	/**
	 * ajax 上传图片并返回文件路径
	 * 
	 * @return
	 * @throws Exception
	 */
	public String upload() throws Exception {
		List<String> mfiles = new ArrayList<String>();
	
		User user=(User)ActionContext.getContext().getSession().get("user");
	
		userid=user.getId();

		//文件重命名，文件保存
		String updatadate=new SimpleDateFormat("yyyyMMddHHmmss")
				.format(new Date());
		String fileName =updatadate+ "_" + uploadifyFileName;
		ActionContext ac = ActionContext.getContext();
		ServletContext sc = (ServletContext) ac
				.get(ServletActionContext.SERVLET_CONTEXT);
		String realPath = sc.getRealPath("/");
		String dest = realPath + savePath + "/" + fileName;
		System.out.println("目标路径:"+dest);
		FileUtils.copyFile(uploadify, new File(dest));
		mfiles.add(savePath + "/" + fileName);

		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		// 返回图片地址，这里我们实际用的时候可能返回实际图片和缩略图，
		//上传后真正显示的时候用缩略图,实际保存地址为原图片地址。那样加载更快
		out.write(mfiles.get(0));
		out.close();
		out.flush();
		
		Photo photo=new Photo();
		photo.setPName(fileName);
		photo.setOwnerId(userid);
		photo.setPaddr(dest);
		photo.setUpdatetime(updatadate);
		photoService.save(photo);
		
		System.out.println("mfiles.get(0):"+mfiles.get(0));
		System.out.println("uploadify:"+uploadify);
		System.out.println("uploadifyFileName:"+uploadifyFileName);
		System.out.println("uploadifyContentType:"+uploadifyContentType);
		return "toPhotoList";
	}

	// 批量保存图片地址

	public String savePath() throws IOException {
		System.out.println("mvUrl:"+mvUrl);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		out.write("保存成功");
		out.close();
		out.flush();
		return null;
	}
	
	
	/** 列表*/
	public String list() throws Exception{
		List<User> userList=userService.findAll();
		ActionContext.getContext().put("userList", userList);
		return "list";
	}
	
	/** 删除*/
	public String delete() throws Exception{
		userService.delete(model.getId());
		return "toList";
	}
	
	/** 添加页面*/
	public String addUI() throws Exception{
		
		
		return "saveUI";
	}
	
	/**添加*/
	public String add() throws Exception{
		User user=userService.findByLoginName(model.getLoginName());
		if(user!=null) {
			addFieldError("regist", "用户名已被注册！");
			return "saveUI";
		}
		else if(model.getPassword().equals("")) 
		{
			addFieldError("regist", "密码不能为空！");
			return "saveUI";
		}
		else{
			String md5Digest=DigestUtils.md5Hex(model.getPassword());
			model.setPassword(md5Digest);
			userService.save(model);
			return "loginUI";
		}
	}
	
	/** 修改页面*/
	public String editUI() throws Exception{
//		User user=userService.getById(model.getId());
//		ActionContext.getContext().getValueStack().push(user);
//		
		return "editUI";
	}
	
	/** 修改*/
	public String edit() throws Exception{
		//从session获取原对象
		User userin = ((User) ActionContext.getContext().getSession().get("user"));
		userin.setName(model.getName());
		userin.setGender(model.getGender());
		userin.setPhoneNumber(model.getPhoneNumber());
		userin.setEmail(model.getEmail());
		userin.setDescription(model.getDescription());
		//保存到数据库
		userService.update(userin);
		System.out.println("修改成功");
		return "editsuccess";
	}
	
	/**初始化密码功能*/
	public String initPassword()throws Exception{
		//从数据库获取原对象
		User user=userService.getById(model.getId());
		
		//设置修改的属性
		String md5Digest=DigestUtils.md5Hex("1234");
		user.setPassword(md5Digest);
		
		//保存到数据库
		userService.update(user);
		
		return "toList";
	}

	
	
	/**登录页面*/
	public String loginUI() throws Exception{
		return "loginUI";
	}
	
	/**登录*/
	public String login() throws Exception{
		User user=userService.findByLoginNameAndPassword(model.getLoginName(),model.getPassword());
		if(user==null) {
			addFieldError("login", "用户名或密码不正确！");
			return "loginUI";
		}
		else {
			//登录用户
			ActionContext.getContext().getSession().put("user", user);
			return "toIndex";
		}		
		
	}
	
	
	
	/**注销*/
	public String logout() throws Exception{
		ActionContext.getContext().getSession().remove("user");
		return "logout";
	}
	
	
	
	//----------------
	
	public File getUploadify() {
		return uploadify;
	}

	public void setUploadify(File uploadify) {
		this.uploadify = uploadify;
	}

	public String getUploadifyFileName() {
		return uploadifyFileName;
	}

	public void setUploadifyFileName(String uploadifyFileName) {
		this.uploadifyFileName = uploadifyFileName;
	}

	public String getUploadifyContentType() {
		return uploadifyContentType;
	}

	public void setUploadifyContentType(String uploadifyContentType) {
		this.uploadifyContentType = uploadifyContentType;
	}


	public String getMvUrl() {
		return mvUrl;
	}

	public void setMvUrl(String mvUrl) {
		this.mvUrl = mvUrl;
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static String getSavepath() {
		return savePath;
	}

	
	
	

	
}
