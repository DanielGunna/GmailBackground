package com.creativityapps.gmailbackgroundlibrary;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.ArrayRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.creativityapps.gmailbackgroundlibrary.util.GmailSender;
import com.creativityapps.gmailbackgroundlibrary.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class BackgroundMail {
    String TAG = "BackgroundMail";
    private String username;
    private String password;
    private String mailto;
    private String subject;
    private String body;
    private String type;
    private String sendingMessage;
    private String sendingMessageSuccess;
    private String sendingMessageError;
    private boolean processVisibility = true;
    private ArrayList<String> attachments = new ArrayList<>();
    private OnSuccessCallback onSuccessCallback;
    private OnFailCallback onFailCallback;

    public final static String TYPE_PLAIN = "text/plain";
    public final static String TYPE_HTML = "text/html";

    public interface OnSuccessCallback {
        void onSuccess();
    }

    public interface OnFailCallback {
        void onFail();
    }

    public BackgroundMail() {

    }

    private BackgroundMail(Builder builder) {
        attachments = builder.attachments;
        username = builder.username;
        password = builder.password;
        mailto = builder.mailto;
        subject = builder.subject;
        body = builder.body;
        type = builder.type;
        setSendingMessage(builder.sendingMessage);
        setSendingMessageSuccess(builder.sendingMessageSuccess);
        setSendingMessageError(builder.sendingMessageError);
        processVisibility = builder.processVisibility;
        setOnSuccessCallback(builder.onSuccessCallback);
        setOnFailCallback(builder.onFailCallback);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public void setGmailUserName(@NonNull String string) {
        this.username = string;
    }

    @NonNull
    public String getGmailUserName() {
        return username;
    }

    public void setGmailPassword(@NonNull String string) {
        this.password = string;
    }

    @NonNull
    public String getGmailPassword() {
        return password;
    }

    public void setType(@NonNull String string) {
        this.type = string;
    }

    @NonNull
    public String getType() {
        return type;
    }

    public void showVisibleProgress(boolean state) {
        this.processVisibility = state;
    }

    public boolean isProgressVisible() {
        return processVisibility;
    }

    public void setMailTo(@NonNull String string) {
        this.mailto = string;
    }

    @NonNull
    public String getMailTo() {
        return mailto;
    }

    public void setFormSubject(@NonNull String string) {
        this.subject = string;
    }

    @NonNull
    public String getFormSubject() {
        return subject;
    }

    public void setFormBody(@NonNull String string) {
        this.body = string;
    }

    @NonNull
    public String getFormBody() {
        return body;
    }

    public void setSendingMessage(@NonNull String string) {
        this.sendingMessage = string;
    }

    @NonNull
    public String getSendingMessage() {
        return sendingMessage;
    }

    public void setSendingMessageSuccess(@Nullable String string) {
        this.sendingMessageSuccess = string;
    }

    @Nullable
    public String getSendingMessageSuccess() {
        return sendingMessageSuccess;
    }

    public void setSendingMessageError(@Nullable String string) {
        this.sendingMessageError = string;
    }


    @Nullable
    public String getSeningMessageError() {
        return sendingMessageError;
    }

    public void addAttachment(@NonNull String attachment) {
        this.attachments.add(attachment);
    }

    public void addAttachments(@NonNull List<String> attachments) {
        this.attachments.addAll(attachments);
    }

    public void addAttachments(String... attachments) {
        this.attachments.addAll(Arrays.asList(attachments));
    }

    @NonNull
    public List<String> getAttachments() {
        return attachments;
    }

    public void setOnSuccessCallback(OnSuccessCallback onSuccessCallback) {
        this.onSuccessCallback = onSuccessCallback;
    }

    public void setOnFailCallback(OnFailCallback onFailCallback) {
        this.onFailCallback = onFailCallback;
    }


    public void send() {

        if (TextUtils.isEmpty(username)) {
            throw new IllegalArgumentException("You didn't set a Gmail username");
        }
        if (TextUtils.isEmpty(password)) {
            throw new IllegalArgumentException("You didn't set a Gmail password");
        }
        if (TextUtils.isEmpty(mailto)) {
            throw new IllegalArgumentException("You didn't set a Gmail recipient");
        }
        if (TextUtils.isEmpty(body)) {
            throw new IllegalArgumentException("You didn't set a body");
        }
        if (TextUtils.isEmpty(subject)) {
            throw new IllegalArgumentException("You didn't set a subject");
        }

        new SendEmailTask().execute();
    }

    public class SendEmailTask extends AsyncTask<String, Void, Boolean> {


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(String... arg0) {
            try {
                GmailSender sender = new GmailSender(username, password);
                if (!attachments.isEmpty()) {
                    for (int i = 0; i < attachments.size(); i++) {
                        if (!attachments.get(i).isEmpty()) {
                            sender.addAttachment(attachments.get(i));
                        }
                    }
                }
                sender.sendMail(subject, body, username, mailto, type);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                if (onSuccessCallback != null) {
                    onSuccessCallback.onSuccess();
                }
            } else {
                if (onFailCallback != null) {
                    onFailCallback.onFail();
                }
            }
        }
    }

    public static final class Builder {

        private String username;
        private String password;
        private String mailto;
        private String subject;
        private String body;
        private String type = BackgroundMail.TYPE_PLAIN;
        private ArrayList<String> attachments = new ArrayList<>();
        private String sendingMessage;
        private String sendingMessageSuccess;
        private String sendingMessageError;
        private boolean processVisibility = true;
        private OnSuccessCallback onSuccessCallback;
        private OnFailCallback onFailCallback;

        private Builder() {

        }

        public Builder withUsername(@NonNull String username) {
            this.username = username;
            return this;
        }


        public Builder withPassword(@NonNull String password) {
            this.password = password;
            return this;
        }


        public Builder withMailto(@NonNull String mailto) {
            this.mailto = mailto;
            return this;
        }


        public Builder withSubject(@NonNull String subject) {
            this.subject = subject;
            return this;
        }


        //set email mime type
        public Builder withType(@NonNull String type) {
            this.type = type;
            return this;
        }


        public Builder withBody(@NonNull String body) {
            this.body = body;
            return this;
        }


        public Builder withAttachments(@NonNull ArrayList<String> attachments) {
            this.attachments.addAll(attachments);
            return this;
        }

        public Builder withAttachments(String... attachments) {
            this.attachments.addAll(Arrays.asList(attachments));
            return this;
        }


        public Builder withSendingMessage(@NonNull String sendingMessage) {
            this.sendingMessage = sendingMessage;
            return this;
        }


        public Builder withSendingMessageSuccess(@Nullable String sendingMessageSuccess) {
            this.sendingMessageSuccess = sendingMessageSuccess;
            return this;
        }


        public Builder withSendingMessageError(@Nullable String sendingMessageError) {
            this.sendingMessageError = sendingMessageError;
            return this;
        }


        public Builder withProcessVisibility(boolean processVisibility) {
            this.processVisibility = processVisibility;
            return this;
        }

        public Builder withOnSuccessCallback(OnSuccessCallback onSuccessCallback) {
            this.onSuccessCallback = onSuccessCallback;
            return this;
        }

        public Builder withOnFailCallback(OnFailCallback onFailCallback) {
            this.onFailCallback = onFailCallback;
            return this;
        }

        public BackgroundMail build() {
            return new BackgroundMail(this);
        }

        public BackgroundMail send() {
            BackgroundMail backgroundMail = build();
            backgroundMail.send();
            return backgroundMail;
        }
    }
}
