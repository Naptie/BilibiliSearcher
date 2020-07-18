package me.naptie.bilibili;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import github.clyoudu.consoletable.ConsoleTable;
import github.clyoudu.consoletable.enums.Align;
import github.clyoudu.consoletable.table.Cell;
import me.naptie.bilibili.objects.Sex;
import me.naptie.bilibili.objects.User;
import me.naptie.bilibili.utils.UserAgentManager;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Main {

    private static boolean debug = false;

    @SuppressWarnings("DuplicatedCode")
    public static void main(String[] args) throws IOException, InterruptedException {
        PrintStream standardOutput = System.out;
        if (args.length >= 3) {
            String keyword = args[0];
            int max = Integer.parseInt(args[1]);
            String order = args[2];
            debug = args.length >= 4 && (args[3].equalsIgnoreCase("debug"));
            String path = args.length >= 5 ? args[4] : (new File("").getAbsolutePath());
            String sex = args.length >= 6 ? args[5] : "";
            boolean male = (sex.toLowerCase().contains("male") && !sex.toLowerCase().contains("female")) || (sex.toLowerCase().contains("man") && !sex.toLowerCase().contains("woman")) || sex.toLowerCase().contains("boy") || sex.contains("男") || sex.contains("\\u7537");
            boolean female = sex.toLowerCase().contains("female") || sex.toLowerCase().contains("woman") || sex.toLowerCase().contains("girl") || sex.contains("女") || sex.contains("\\u5973");
            boolean unset = sex.toLowerCase().contains("unset") || sex.toLowerCase().contains("unknown") || sex.contains("保密") || sex.contains("\\u4fdd\\u5bc6");
            /*List<User> maleUsers = new ArrayList<>();
            List<User> femaleUsers = new ArrayList<>();
            List<User> unknownUsers = new ArrayList<>();*/
            List<User> users = new ArrayList<>();
            Set<Long> mids = new HashSet<>();
            int counter = 0;
            if (!debug) System.out.println("正在搜索，请稍候...");
            while (users.size() < max) {
                counter++;
                if (counter > 50) break;
                JSONObject result = readJsonFromUrl("http://api.bilibili.com/x/web-interface/search/type?keyword=" + keyword.replaceAll(" ", "%20") + "&search_type=video&order=" + order + "&duration=0&page=" + counter + "&tids=0");
                JSONArray videos = result.getJSONObject("data").getJSONArray("result");
                for (int i = 0; i < videos.size(); i++) {
                    JSONObject video = videos.getJSONObject(i);
                    long mid = video.getLong("mid");
                    if (!mids.contains(mid)) {
                        mids.add(mid);
                        JSONObject userJson = readJsonFromUrl("http://api.bilibili.com/x/web-interface/card?mid=" + mid);
                        JSONObject card = userJson.getJSONObject("data").getJSONObject("card");
                        User user = new User(card.getString("name"), mid, card.getString("sex"), card.getJSONObject("level_info").getIntValue("current_level"), userJson.getJSONObject("data").getIntValue("archive_count"), card.getIntValue("fans"), video.getString("title").replace("<em class=\"keyword\">", "").replace("</em>", ""), video.getString("bvid"));
                        if (user.getSex() == Sex.MALE && male) {
                            users.add(user);
                        } else if (user.getSex() == Sex.FEMALE && female) {
                            users.add(user);
                        } else if (user.getSex() == Sex.UNSET && unset) {
                            users.add(user);
                        }
                    }
                }
            }
            if (counter > 50) System.out.println("设置的结果数" + max + "太大，仅找到" + users.size() + "个结果");
            /*if (sex.equalsIgnoreCase("male") || sex.equalsIgnoreCase("man") || sex.equalsIgnoreCase("boy") || sex.equals("男") || sex.equals("\\u7537")) {
                users = maleUsers;
            } else if (sex.equalsIgnoreCase("female") || sex.equalsIgnoreCase("woman") || sex.equalsIgnoreCase("girl") || sex.equals("女") || sex.equals("\\u5973")) {
                users = femaleUsers;
            } else if (sex.equalsIgnoreCase("unset") || sex.equalsIgnoreCase("unknown") || sex.equals("保密") || sex.equals("\\u4fdd\\u5bc6")) {
                users = unknownUsers;
            } else {
                if (male && female && unset) {
                    users.addAll(maleUsers);
                    users.addAll(femaleUsers);
                    users.addAll(unknownUsers);
                } else if (male && female) {
                    users.addAll(maleUsers);
                    users.addAll(femaleUsers);
                } else if (male && unset) {
                    users.addAll(maleUsers);
                    users.addAll(unknownUsers);
                } else if (female && unset) {
                    users.addAll(femaleUsers);
                    users.addAll(unknownUsers);
                } else {
                    users.addAll(maleUsers);
                    users.addAll(femaleUsers);
                    users.addAll(unknownUsers);
                }
            }*/
            List<Cell> header = new ArrayList<>() {{
                add(new Cell("ID"));
                add(new Cell("UID"));
                add(new Cell("性别"));
                add(new Cell("等级"));
                add(new Cell("稿件数"));
                add(new Cell("粉丝数"));
                add(new Cell("视频BV号"));
                add(new Cell("视频标题"));
            }};
            List<List<Cell>> bodyForDisplay = new ArrayList<>() {{
                for (User user : users) {
                    add(new ArrayList<>() {{
                        add(new Cell(user.getName()));
                        add(new Cell(Align.RIGHT, user.getMid() + ""));
                        add(new Cell(user.getSex().getText()));
                        add(new Cell("lv" + user.getLevel()));
                        add(new Cell(Align.RIGHT, user.getArchives() + ""));
                        add(new Cell(Align.RIGHT, user.getFans() + ""));
                        add(new Cell(user.getBvid()));
                        add(new Cell(user.getVidTitle()));
                    }});
                }
            }};
            List<List<Cell>> body = new ArrayList<>() {{
                for (User user : users) {
                    add(new ArrayList<>() {{
                        add(new Cell(user.getName()));
                        add(new Cell(Align.RIGHT, user.getMid() + ""));
                        add(new Cell(user.getSex().getText()));
                        add(new Cell("lv" + user.getLevel()));
                        add(new Cell(Align.RIGHT, user.getArchives() + ""));
                        add(new Cell(Align.RIGHT, user.getFans() + ""));
                        add(new Cell(user.getBvid()));
                        add(new Cell(user.getVideoTitle()));
                    }});
                }
            }};
            System.out.println("以" + getOrderText(order) + "为序的前" + users.size() + "个“" + keyword + "”搜索结果：");
            new ConsoleTable.ConsoleTableBuilder().addHeaders(header).addRows(bodyForDisplay).build().print();
            System.out.println("程序作者：Naptie；QQ群：345515864；B站：https://space.bilibili.com/474403243；Discord：https://discord.gg/sfp87w7");
            try {
                File file = new File(new File(path), "results_" + keyword.replaceAll(" ", "-") + "_" + order + "_" + max + "_" + getCurrentFormattedDate("yyyy-MM-dd_HH-mm-ss") + ".txt");
                //noinspection ResultOfMethodCallIgnored
                file.createNewFile();
                PrintStream stream = new PrintStream(file);
                System.setOut(stream);
                if (counter > 50) System.out.println("设置的结果数" + max + "太大，仅找到" + users.size() + "个结果");
                System.out.println("以" + getOrderText(order) + "为序的前" + users.size() + "个“" + keyword + "”搜索结果：");
                new ConsoleTable.ConsoleTableBuilder().addHeaders(header).addRows(body).build().print();
                System.out.println("程序作者：Naptie；QQ群：345515864；B站：https://space.bilibili.com/474403243；Discord：https://discord.gg/sfp87w7");
                System.setOut(standardOutput);
                System.out.println("本次搜索记录成功保存至 " + file.getAbsolutePath());
            } catch (IOException e) {
                System.out.println("保存本次搜索记录时出错");
                e.printStackTrace();
            }
        }
    }

    private static JSONObject readJsonFromUrl(String url) throws IOException, InterruptedException {
        String userAgent = UserAgentManager.getUserAgent();
        if (debug) System.out.println("正在访问 " + url + "，使用 UA “" + userAgent + "”");
        URLConnection request = (new URL(url)).openConnection();
        request.setRequestProperty("User-Agent", userAgent);
        request.connect();
        TimeUnit.MILLISECONDS.sleep(200);
        return JSON.parseObject(IOUtils.toString((InputStream) request.getContent(), StandardCharsets.UTF_8));
    }

    private static String getCurrentFormattedDate(String format) {
        Date date = new Date();
        SimpleDateFormat ft = new SimpleDateFormat(format);
        return ft.format(date);
    }

    private static String getOrderText(String order) {
        return switch (order) {
            case "totalrank" -> "综合排序";
            case "click" -> "最多点击";
            case "pubdate" -> "最新发布";
            case "dm" -> "最多弹幕";
            case "stow" -> "最多收藏";
            case "scores" -> "最多评论";
            default -> "未知";
        };
    }
}
