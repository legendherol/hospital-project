package com.ruoyi.hospital.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AesRsaKeyDto {

    private String aesTransferKey;

    private String userPrivateKey;
}
