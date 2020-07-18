package me.naptie.bilibili.objects;

public class User {

    private String name;
    private long mid;
    private Sex sex;
    private int level;
    private int archives;
    private int fans;
    private String videoTitle;
    private String bvid;

    public User(String name, long mid, Sex sex, int level, int archives, int fans, String vidTitle, String bvid) {
        this.name = name;
        this.mid = mid;
        this.sex = sex;
        this.level = level;
        this.archives = archives;
        this.fans = fans;
        this.videoTitle = vidTitle;
        this.bvid = bvid;
    }
    public User(String name, long mid, String sex, int level, int archives, int fans, String videoTitle, String bvid) {
        this.name = name;
        this.mid = mid;
        this.level = level;
        this.archives = archives;
        this.fans = fans;
        this.videoTitle = videoTitle;
        this.bvid = bvid;
        if (sex.equals("男") || sex.equals("\\u7537")) {
            this.sex = Sex.MALE;
        } else if (sex.equals("女") || sex.equals("\\u5973")) {
            this.sex = Sex.FEMALE;
        } else {
            this.sex = Sex.UNSET;
        }
    }

    public String getName() {
        return this.name;
    }

    public long getMid() {
        return this.mid;
    }

    public Sex getSex() {
        return this.sex;
    }

    public int getLevel() {
        return this.level;
    }

    public int getArchives() {
        return this.archives;
    }

    public int getFans() {
        return this.fans;
    }

    public String getVidTitle() {
        if (videoTitle.length() <= 22) {
            return videoTitle;
        } else {
            return videoTitle.substring(0, 21) + "...";
        }
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public String getBvid() {
        return bvid;
    }
}
