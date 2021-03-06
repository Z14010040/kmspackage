package com.sansec.kmspackage.result;

public class CodeMsg {
	
	private int code;
	private String msg;
//    通用模块
	public static CodeMsg SUCCESS = new CodeMsg(0, "success");
	public static CodeMsg PARAMETER_VALID_ERROR = new CodeMsg(500001, "参数校验异常：%s");
	public static CodeMsg SERVER_ERROR = new CodeMsg(500002, "服务端异常");
	//文件上传
	public static CodeMsg UPLOAD_SUCCESS = new CodeMsg(0, "文件上传成功");    //只是为了打印日志
	public static CodeMsg UPLOAD_ERROR = new CodeMsg(500100, "文件上传失败：%s");

	//文件下载
	public static CodeMsg DOWNLOAD_SUCCESS = new CodeMsg(0, "文件下载成功");  //只是为了打印日志
	public static CodeMsg DOWNLOAD_ERROR = new CodeMsg(500200, "文件下载失败：%s");


	//打包
	public static CodeMsg PACKAGE_SUCCESS = new CodeMsg(0, "打包成功");  //只是为了打印日志
	public static CodeMsg PACKAGE_ERROR = new CodeMsg(500300, "打包失败：%s");
	public static CodeMsg VERIFY_SIGN_ERROR = new CodeMsg(500301, "验证签名失败：%s");

	//sql执行结果
	public static CodeMsg SQL_SUCCESS = new CodeMsg(0, "SQL文件测试正确");  //只是为了打印日志
	public static CodeMsg SQL_ERROR = new CodeMsg(500400, "SQL文件测试有误");

//	其他模块
    public static CodeMsg UNZIP_HADOOPKMS_ERROR = new CodeMsg(500401, "解压HadoopKMS.zip出错");
    public static CodeMsg UNZIP_REST_ERROR = new CodeMsg(500401, "解压Rest.zip出错");
    public static CodeMsg UNZIP_SYSFILE_ERROR = new CodeMsg(500401, "解压sysfile.zip出错");

	private CodeMsg( ) {
	}
			
	private CodeMsg(int code, String msg ) {
		this.code = code;
		this.msg = msg;
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public CodeMsg fillArgs(Object... args) {
		int code = this.code;
		String message = String.format(this.msg, args);
		return new CodeMsg(code, message);
	}

	@Override
	public String toString() {
		return "CodeMsg [code=" + code + ", msg=" + msg + "]";
	}
	
	
}
