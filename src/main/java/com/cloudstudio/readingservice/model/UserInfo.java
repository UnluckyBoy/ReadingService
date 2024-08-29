package com.cloudstudio.readingservice.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName UserInfo
 * @Author Create By matrix
 * @Date 2024/8/27 22:53
 */
@Data
public class UserInfo implements Serializable {
    private int mId;
    private String mHead;
    private String mName;
    private String mPassword;
    private String mSex;
    private String mAccount;
    private String mPhone;
    private String mEmail;
    private int mLevel;
    private int mStatus;
    private String mAddressIp;
}
