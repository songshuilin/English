package net.naucu.englishxianshi.ui.account.data;

public interface IUserInfoDao {

    /**
     * 注册验证码
     *
     * @param telephone
     */
    void registerSmsCode(String telephone, ResponseListener<Integer> responseListener);

    /**
     * 注册用户
     *
     * @param telephone
     * @param password
     * @param smsCode
     */
    void registerUser(String telephone, String password, int smsCode, String szImei , ResponseListener<Integer> responseListener);

    /**
     * 修改密匙验证码
     *
     * @param telephone
     */
    void updatePasswordSmsmCode(String telephone, ResponseListener<Integer> responseListener);

    /**
     * 修改密匙
     *
     * @param telephone
     * @param password
     * @param smsCode
     */
    void updatePassword(String telephone, String password, int smsCode, ResponseListener<Integer> responseListener);

    /**
     * 修改手机号
     *
     * @param myTelephone
     * @param newTelephone
     * @param responseListener
     */
    void updatePhone(String myTelephone, String newTelephone, ResponseListener<Integer> responseListener);
}
