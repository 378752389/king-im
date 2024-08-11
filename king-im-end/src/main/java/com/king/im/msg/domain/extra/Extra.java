package com.king.im.msg.domain.extra;

import lombok.Data;

@Data
public class Extra {

    private VideoExtra videoExtra;

    private AudioExtra audioExtra;

    private PictureExtra pictureExtra;

    private FileExtra fileExtra;
}
