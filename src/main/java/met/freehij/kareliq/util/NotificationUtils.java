package met.freehij.kareliq.util;

import met.freehij.loader.util.InjectionHelper;

import java.util.LinkedList;

public class NotificationUtils {
    public static final LinkedList<Notification> notifications = new LinkedList<>();

    static {
        new Thread(() -> {
            while (true) {
                synchronized (notifications) {
                    Notification n = notifications.peek();
                    if (n == null) continue;
                    if (n.creationTime + n.decayTime < System.currentTimeMillis()) notifications.remove(n);
                }
            }
        }).start();
    }

    public static void drawNotifications(InjectionHelper helper) {
        try {
            int height = Utils.createScaledResolution(InjectionHelper.getMinecraft()).invoke("getScaledHeight").getInt();
            int width = Utils.createScaledResolution(InjectionHelper.getMinecraft()).invoke("getScaledWidth").getInt();
            int i = 0;
            synchronized (notifications) {
                for (Notification n : notifications) {
                    if (i > 4) return;
                    helper.getSelf().invoke("drawRect", width - 2, height - 2 - (i * 24), width - 250, height - 24 - (i * 24), 0xaa000000);
                    InjectionHelper.getMinecraft().getField("fontRenderer").invoke("drawStringWithShadow", n.text, width - 246, height - 17 - (i * 24), 0xffffff);
                    i++;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static class Notification {
        public final String text;
        public final long creationTime;
        public final long decayTime;

        public Notification(String text, long decayTime) {
            this.text = text;
            this.creationTime = System.currentTimeMillis();
            this.decayTime = decayTime;
        }
    }
}
