package site.zqiusu.service.weixin;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import site.zqiusu.domain.req.WeixinQrCodeReq;
import site.zqiusu.domain.res.WeixinQrCodeRes;
import site.zqiusu.domain.res.WeixinTokenRes;
import site.zqiusu.domain.vo.WeixinTemplateMessageVO;

public interface IWeixinApiService {

    @GET("cgi-bin/token")
    Call<WeixinTokenRes> getToken(@Query("grant_type") String grantType,
                                  @Query("appid") String appId,
                                  @Query("secret") String appSecret);

    @POST("cgi-bin/qrcode/create")
    Call<WeixinQrCodeRes> createQrCode(@Query("access_token") String accessToken, @Body WeixinQrCodeReq weixinQrCodeReq);

    @POST("cgi-bin/message/template/send")
    Call<Void> sendMessage(@Query("access_token") String accessToken, @Body WeixinTemplateMessageVO weixinTemplateMessageVO);

}
