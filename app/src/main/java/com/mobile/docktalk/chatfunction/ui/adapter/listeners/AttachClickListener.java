package com.mobile.docktalk.chatfunction.ui.adapter.listeners;

import com.quickblox.chat.model.QBAttachment;

public interface AttachClickListener {

    void onLinkClicked(QBAttachment attachment, int positionInAdapter);
}