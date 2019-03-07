package com.sucre.controller;

import com.sucre.dao.vidDao;
import com.sucre.entity.Weibo;
import com.sucre.factor.Factor;
import com.sucre.jdbc.JdbcConnector;
import com.sucre.listUtil.MutiList;
import com.sucre.service.*;
import com.sucre.utils.*;

import javax.swing.*;

public class Controller {

    public static Controller controller = new Controller();
    private WeiboImpl weiboImplId;
    private WeiboImpl weiboImplCookie;
    private VidImpl vidImpl;
    private boolean stop = false;

    private Controller() {
    }

    /**
     * 导入ip文件配置。
     */
    public void load() {
        try {
            // 导入换ip配置
            Info info = accounts.getInstance();
            MyUtil.loadADSL("adsl.properties", accounts.getInstance());
            MyUtil.print(info.getADSL() + "<>" + info.getADSLname() + "<>" + info.getADSLpass(), Factor.getGui());
            // 加载js
            JsUtil.loadJs("js.js");
            JdbcConnector.Load("jdbc.properties");
        } catch (Exception e) {
            MyUtil.print("导入文件出错：" + e.getMessage(), Factor.getGui());
        }
    }

    /**
     * 加载指定文件到账号列表
     *
     * @param fileName
     */
    public void loadWeiboId(String fileName, JTable table, String mission) {
        weiboImplId = new WeiboImpl();
        weiboImplId.loadList(fileName);
        // weiboImplId.getCounts(Integer.parseInt(fileName), mission);
        GuiUtil.loadTableVid(table, (MutiList) weiboImplId.getlist());
    }

    /**
     * 加载指定文件到cookie列表
     *
     * @param fileName
     */
    public void loadWeiboCookie(String fileName, JTable table) {
        weiboImplCookie = new WeiboImpl();
        weiboImplCookie.loadList(fileName);
        GuiUtil.loadTableVid(table, (MutiList) weiboImplCookie.getlist());
    }

    /**
     * 加载指定文件到vid列表
     *
     * @param fileName
     * @return
     */
    public void loadVid(String fileName, JTable table) {
        vidImpl = new VidImpl();
        vidImpl.loadVid(fileName);
        GuiUtil.loadTableVid(table, (MutiList) vidImpl.getlist());

    }

    /**
     * 添加一个vid
     *
     * @param data
     * @param table
     */
    public void addVid(String data, JTable table) {
        if (null == vidImpl) {
            vidImpl = new VidImpl();
        }
        vidImpl.add(data);
        GuiUtil.loadTableVid(table, (MutiList) vidImpl.getlist());
    }

    /**
     * 取vid的列表对象
     *
     * @return
     */
    public vidDao getVidImpl() {
        return vidImpl;
    }

    /**
     * 加载list到列表
     *
     * @param table
     * @param list
     */
    public void loadTable(JTable table, MutiList list) {
        GuiUtil.loadTable(table, list);
    }

    public void addCookie(Weibo weibo) {
        if (weiboImplCookie == null) {
            weiboImplCookie = new WeiboImpl();
        }
        weiboImplCookie.add(weibo);
    }

    /**
     * 调用批量登录功能
     *
     * @param thread   线程数量
     * @param limit    账号总数
     * @param isCircle 是否循环
     */
    public void login(int thread, boolean isCircle, int start) {
        int limit = weiboImplId == null ? 0 : weiboImplId.getsize();
        if (limit == 0) {
            MyUtil.print("ID未导入！", Factor.getGui());
        }
        Login login = new Login(start, limit - 1, isCircle, weiboImplId);
        for (int i = 1; i <= thread; i++) {

            Thread t = new Thread(login);
            if (i == 1) {
                t.setName("ip");
            }
            t.start();
        }
    }

    public void guess(int startpoint, int thread, boolean isCircle) {
        int limit = weiboImplCookie == null ? 0 : weiboImplCookie.getsize();
        if (limit == 0) {
            MyUtil.print("Cookie未导入！", Factor.getGui());
        }
        Guess guess = new Guess(startpoint, limit - 1, isCircle, weiboImplCookie);
        for (int i = 1; i <= thread; i++) {

            Thread t = new Thread(guess);
            if (i == 1) {
                t.setName("ip");
            }
            t.start();
        }
    }

    /**
     * 取九宫格验证码
     *
     * @param thread
     * @param isCircle
     */
    public void getPic(int thread, boolean isCircle) {
        int limit = weiboImplId == null ? 0 : weiboImplId.getsize();
        if (limit == 0) {
            MyUtil.print("Id未导入！", Factor.getGui());
        }
        Capcha capcha = new Capcha(0, limit, isCircle, weiboImplId);
        for (int i = 1; i <= thread; i++) {

            Thread t = new Thread(capcha);
            if (i == 1) {
                t.setName("ip");
            }
            t.start();
        }
    }

    /**
     * 新浪投票类
     *
     * @param start    起始位置
     * @param thread   线程数
     * @param isCircle 是否循环
     * @param mission  任务名称
     */
    public void vote(int start, int thread, boolean isCircle, String mission) {
        int limit = weiboImplCookie == null ? 0 : weiboImplCookie.getsize();
        if (limit == 0 || getVidImpl() == null) {
            MyUtil.print("Cookie或者vid未导入！", Factor.getGui());
            return;
        }
        SinaVote vote = new SinaVote(start, limit - 1, isCircle, weiboImplCookie, mission);
        for (int i = 1; i <= thread; i++) {

            Thread t = new Thread(vote);
            if (i == 1) {
                t.setName("ip");
            }
            t.start();
        }
    }

    /**
     * 取用户定义的多少账号换一次ip
     *
     * @return
     */
    public int changeIPcount() {
        return Factor.getGuiFrame().getIPcount();
    }

    /**
     * 定时换ip,秒数
     */
    public void changeipM(String mission, int s) {
        Thread thread = new Thread() {
            int i = 0;

            public void run() {
                while (true) {
                    i++;
                    if (i == s) {
                        MyUtil.changIp();
                        i = 0;
                    }
                    MyUtil.sleeps(1000);
                }
            }

            ;
        };
        thread.start();
    }

    public void doMission(String mission, int l, int thread, boolean isCircle) {

        switch (mission) {
            case "checkin":
                int limit = weiboImplCookie == null ? 0 : weiboImplCookie.getsize();
                if (limit == 0) {
                    MyUtil.print("Cookie或者vid未导入！", Factor.getGui());
                }
                for (int i = 1; i <= thread; i++) {
                    CheckIn checkin = new CheckIn(l, limit - 1, isCircle, weiboImplCookie, mission);
                    Thread t = new Thread(checkin);
                    if (i == 1) {
                        t.setName("ip");
                    }
                    t.start();
                }
                break;

            default:
                break;
        }

    }

    /**
     * 拿到controller的对象实例。
     *
     * @return
     */
    public static Controller getInstance() {
        return controller;
    }

    /**
     * 刷新指定数据列表
     *
     * @param cookietable
     */
    public void refresh(JTable table) {
        if (weiboImplCookie == null) {
            return;
        }
        GuiUtil.loadTableVid(table, (MutiList) weiboImplCookie.getlist());
    }

    /**
     * 刷新指定数据列表(线程回调)
     *
     * @param cookietable
     */
    public void refresh() {
        Factor.getGuiFrame().refresh();
    }

    /**
     * 继续任务
     */
    public void resume() {
        this.stop = false;
    }

    /**
     * 暂停任务
     */
    public void stop() {
        this.stop = true;
    }

    /**
     * 判断是否为暂停状态
     *
     * @return 当前状态。
     */
    public boolean isStop() {
        return this.stop;
    }

    public int getEndCount() {
        return Factor.getGuiFrame().getCounts();
    }

    /**
     * 返回列表对象，适应打码时的对象（weiboCapcha）要求。
     * @return
     */
    public WeiboImpl getWeibo(){
        return this.weiboImplCookie;
    }
}
