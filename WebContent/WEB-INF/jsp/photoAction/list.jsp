<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%pageContext.setAttribute("baseURL", request.getContextPath()); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<%@ include file="/WEB-INF/jsp/public/commons.jspf" %>
	<script src="http://how2j.cn/study/js/jquery/2.0.0/jquery.min.js"></script>
	<link href="http://how2j.cn/study/css/bootstrap/3.3.6/bootstrap.min.css" rel="stylesheet">
	<script src="http://how2j.cn/study/js/bootstrap/3.3.6/bootstrap.min.js"></script>
	<script src="${baseURL }/js/bootstrap-paginator.js"></script>
	<style>
	.pagination{
		cursor: pointer; 
	}
	</style>
	<script type="text/javascript">
    $(function(){
        var options={
            bootstrapMajorVersion:1,    //版本
            currentPage:1,    //当前页数
            numberOfPages:5,    //最多显示Page页
            totalPages:10,    //所有数据可以显示的页数
                itemTexts: function (type, page, current) {
            	      switch (type) {
            	        case "first":
            	          return "首页";
            	        case "prev":
            	          return "上一页";
            	        case "next":
            	          return "下一页";
            	        case "last":
            	          return "末页";
            	        case "page":
            	          return page;
            	      }
            	},
            onPageClicked:function(e,originalEvent,type,page){
                console.log("e");
                console.log(e);
                console.log("originalEvent");
                console.log(originalEvent);
                console.log("type");
                console.log(type);
                console.log("page");
                console.log(page);
            }
        }
        $("#page").bootstrapPaginator(options);
    })
	</script>
</head>
<body>
	
	这是照片展示模块！！！！！！！！！！！！！！！
	<br>
	<s:a action="photo_add">上传照片</s:a>
	<br>
	<s:iterator value="#photoList">
		<img src="/ElectronicAlbum/imgs/${PName}" name="mvUrl" width="100px" height="80px" class="myimg"/>
		<s:property value="PName"/>
		
		<s:a action="photo_delete?id=%{Id}" onclick="return confirm('确定要删除吗?')">删除</s:a>
		<br>
		
	</s:iterator>
<<<<<<< HEAD
	<div class="rank">排序方法</div>
	<div class="photopage">所有照片</div>

	  <div class="pagination" id="page"> </div>
=======
	
	
	<table>
		<tr>
			<td>photoid</td>
			<td>photoPName</td>
			<td>photoupdatetime</td>
		</tr>
    	<s:iterator value="#photoList" id="p">  
	    	<tr>  
	        <td><s:property value="#p.Id"></s:property></td>  
	        <td><s:property value="#p.PName"></s:property></td>
	        <td><s:property value="#p.updatetime"></s:property></td>  
	        </tr>  
    	</s:iterator>
		
		<span style="color:#FF0000;"><s:set name="page" value="#session.pagePhoto"></s:set>  
        <tr>  
        <td colspan="9">  
        当前是第<s:property value="#session.pagePhoto.pageNow"/>页，共<s:property value="#session.pagePhoto.totalPage"/>页  
        <s:if test="#session.pagePhoto.hasFirst">  
            <s:a action="photo_getAllPhotos?pageNow=1" >首页</s:a>  
        </s:if>  
        <s:if test="#session.pagePhoto.hasPre">  
            <s:a action="photo_getAllPhotos?pageNow=1" >上一页</s:a>  
        </s:if>  
        <s:if test="#session.pagePhoto.hasNext">  
            <s:a action="photo_getAllPhotos?pageNow=2" >下一页</s:a>  
        </s:if>  
         <s:if test="#session.pagePhoto.hasLast">  
            <s:a action="photo_getAllPhotos?pageNow=2" >尾页</s:a>  
        </s:if>  
     	</td>  
  		</tr>
  		</span>	
	
	
	</table>
	
	
	
>>>>>>> 409d0ea3c139e0b36ce2b46acebcbe35a2f90a99
</body>
</html>