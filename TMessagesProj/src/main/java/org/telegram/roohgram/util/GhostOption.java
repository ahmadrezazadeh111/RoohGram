package org.telegram.roohgram.util;

import android.content.Context;
import android.content.SharedPreferences;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.MessagesController;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

public class GhostOption {
    private static GhostOption instance;

    public static GhostOption getInstance() {
        if (instance == null) {
            instance = new GhostOption();
        }
        return instance;
    }


    public void setGhostMode(boolean mode) {
        SharedPreferences.Editor shareEdit = ApplicationLoader.applicationContext.getSharedPreferences("ghost", Context.MODE_PRIVATE).edit();
        shareEdit.putBoolean("ghost", mode).commit();
        TLRPC.TL_account_setPrivacy req = new TLRPC.TL_account_setPrivacy();
        req.key = new TLRPC.TL_inputPrivacyKeyStatusTimestamp();
        req.rules.add(mode ? new TLRPC.TL_inputPrivacyValueDisallowAll() : new TLRPC.TL_inputPrivacyValueAllowAll());
        ConnectionsManager.getInstance().sendRequest(req, new RequestDelegate() {
            @Override
            public void run(final TLObject response, final TLRPC.TL_error error) {
                AndroidUtilities.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        if (error == null) {
                            TLRPC.TL_account_privacyRules rules = (TLRPC.TL_account_privacyRules) response;
                            MessagesController.getInstance().putUsers(rules.users, false);
                            ContactsController.getInstance().setPrivacyRules(rules.rules, 0);
                        }
                    }
                });
            }
        }, ConnectionsManager.RequestFlagFailOnServerErrors);
    }

    public boolean getGhostMode() {
        return ApplicationLoader.applicationContext.getSharedPreferences("ghost", Context.MODE_PRIVATE).getBoolean("ghost", false);
    }

}
