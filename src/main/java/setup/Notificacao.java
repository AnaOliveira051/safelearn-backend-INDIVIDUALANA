package setup;

import java.awt.*;

public class Notificacao {
    public Boolean NotificationVerification(){
        Boolean supported = true;
        if (!SystemTray.isSupported()) {
            System.out.println("Bandeja do sistema não é suportada!");
            supported = false;
        }

        return supported;
    }

    public void SendNotification(String titulo){
        Boolean supported = NotificationVerification();

        if (supported.equals(true)){

            SystemTray tray = SystemTray.getSystemTray();
            java.awt.Image image = Toolkit.getDefaultToolkit().createImage("../public/logoSafePrint.png");

            TrayIcon trayIcon = new TrayIcon(image, "Notificação");

            try{
                tray.add(trayIcon);
            }
            catch (AWTException e){
                System.out.println("Erro ao adicionar o icone à bandeja do Sistema");
                e.getMessage();
                e.printStackTrace();
            }

            trayIcon.displayMessage("Alerta SafeLearn!!", """
                    Você está acessando uma janela, que não tem permissão: %s. Para ter acesso fale com um admistrador \nEtá janela está sendo fechada...""".formatted(titulo), TrayIcon.MessageType.INFO);

        }else{
            System.out.println("Bandeja do sistema não suportada");
        }
    }
}
