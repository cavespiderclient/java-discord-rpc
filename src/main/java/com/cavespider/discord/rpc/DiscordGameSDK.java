package com.cavespider.discord.rpc;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

public interface DiscordGameSDK extends Library {
    DiscordGameSDK INSTANCE = Native.load("discord_game_sdk", DiscordGameSDK.class);

    class DiscordCreateParams extends Structure {
        public int clientID;
        public int flags;
        public Pointer events;
        public Pointer userData;

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("clientID", "flags", "events", "userData");
        }
    }

    class DiscordUser extends Structure {
        public String userId;
        public String username;
        public String discriminator;
        public String avatar;

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("userId", "username", "discriminator", "avatar");
        }
    }

    class DiscordActivity extends Structure {
        public String state;
        public String details;
        public long startTimestamp;
        public long endTimestamp;
        public String largeImageKey;
        public String largeImageText;
        public String smallImageKey;
        public String smallImageText;
        public String partyId;
        public int partySize;
        public int partyMax;
        public String matchSecret;
        public String joinSecret;
        public String spectateSecret;
        public byte instance;

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("state", "details", "startTimestamp", "endTimestamp", "largeImageKey", "largeImageText", "smallImageKey", "smallImageText", "partyId", "partySize", "partyMax", "matchSecret", "joinSecret", "spectateSecret", "instance");
        }
    }

    class DiscordActivityManager extends Structure {
        public interface OnActivityJoin extends Callback {
            void accept(String secret);
        }

        public interface OnActivitySpectate extends Callback {
            void accept(String secret);
        }

        public interface OnActivityJoinRequest extends Callback {
            void accept(DiscordUser request);
        }

        public interface OnActivityInvite extends Callback {
            void accept(int type, DiscordUser user, DiscordActivity activity);
        }

        public Pointer updateActivity;
        public Pointer clearActivity;
        public Pointer sendRequestReply;
        public Pointer sendInvite;
        public Pointer acceptInvite;

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("updateActivity", "clearActivity", "sendRequestReply", "sendInvite", "acceptInvite");
        }
    }

    class DiscordCore extends Structure {
        public Pointer getUserManager;
        public Pointer getActivityManager;
        public Pointer getRelationshipManager;
        public Pointer getLobbyManager;
        public Pointer getNetworkManager;
        public Pointer getOverlayManager;
        public Pointer getStorageManager;
        public Pointer getStoreManager;
        public Pointer getVoiceManager;
        public Pointer getAchievementManager;
        public Pointer getApplicationManager;
        public Pointer getImageManager;

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("getUserManager", "getActivityManager", "getRelationshipManager", "getLobbyManager", "getNetworkManager", "getOverlayManager", "getStorageManager", "getStoreManager", "getVoiceManager", "getAchievementManager", "getApplicationManager", "getImageManager");
        }
    }

    int DiscordCreate(int clientID, int flags, PointerByReference core);
    void DiscordDestroy(Pointer core);
    void DiscordRunCallbacks(Pointer core);
    void DiscordRegister(String applicationId, String command);
    void DiscordUnregister();
}
