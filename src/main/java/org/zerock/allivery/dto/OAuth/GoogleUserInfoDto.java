package org.zerock.allivery.dto.OAuth;

import lombok.Data;

@Data
public class GoogleUserInfoDto {

    private String id;
    private String email;
    private Boolean verified_email;
    private String name;
    private String given_name;
    private String picture;
    private String locale;

    //   "id": "117349387432109992647",
    //  "email": "1000playch@gmail.com",
    //  "verified_email": true,
    //  "name": "Play_ch",
    //  "given_name": "Play_ch",
    //  "picture": "https://lh3.googleusercontent.com/a-/ACNPEu9Cai-r5RyNQ0Ta2XbFf67cijWEp-Xgz7csE3xZ=s96-c",
    //  "locale": "ko"


}
