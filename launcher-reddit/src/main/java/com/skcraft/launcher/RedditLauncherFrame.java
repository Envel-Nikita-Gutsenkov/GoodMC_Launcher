/*
 * SKCraft Launcher
 * Copyright (C) 2010-2014 Albert Pham <http://www.sk89q.com> and contributors
 * Please see LICENSE.txt for license information.
 */

package com.skcraft.launcher;

import com.skcraft.launcher.dialog.LauncherFrame;
import com.skcraft.launcher.swing.ActionListeners;
import com.skcraft.launcher.swing.ColoredButton;
import com.skcraft.launcher.swing.SwingHelper;
import com.skcraft.launcher.swing.WebpagePanel;
import com.skcraft.launcher.windows.barOverlay;
import lombok.NonNull;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class RedditLauncherFrame extends LauncherFrame {

    private final EmbeddedMediaPlayerComponent mediaPlayerComponent;

    private static final int width = 1200;
    private static final int height = 900;

    public RedditLauncherFrame(@NonNull final Launcher launcher) {
        super(launcher);
        setPreferredSize(new Dimension(width, height));
        setLocationRelativeTo(null);
        dispose();
        setUndecorated(true);

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        mediaPlayerComponent = new EmbeddedMediaPlayerComponent(); //Create EmbeddedMediaPlayer
        contentPane.add(mediaPlayerComponent);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mediaPlayerComponent.release();
                System.exit(0);
            }
        });

        setContentPane(contentPane);
        setVisible(true);

        MediaPlayer mediaPlayer = mediaPlayerComponent.getMediaPlayer();
        barOverlay overlay = new barOverlay(SwingUtilities.getWindowAncestor(contentPane)); //apply overlay

        JPanel root = new JPanel();
        root.setOpaque(false);
        //top layered pane
        JLayeredPane topLayers = new JLayeredPane();
            //create top bar panel
            JPanel topBar = new JPanel();
            topBar.setOpaque(false);
            topBar.setLayout(new BorderLayout());
            topBar.add(getImage("/com/skcraft/launcher/bar_top.png", 1200, -1));
            topBar.setBounds(0,0,1050,148);
            topLayers.add(topBar, 1);

            //create logo button
            JButton logo = new JButton(new ImageIcon(RedditLauncher.class.getResource("/com/skcraft/launcher/logo.png")));
            logo.setContentAreaFilled(false);
            logo.setBorderPainted(false);
            logo.setFocusPainted(false);
            logo.setLayout(new BorderLayout());
            logo.setBounds(410,20,200,208);
            logo.addActionListener(ActionListeners.openURL(this, "https://conquestreforged.com/"));
            topLayers.add(logo, 0);

            //create news button
            ImageIcon newsIcon = new ImageIcon(RedditLauncher.class.getResource("/com/skcraft/launcher/button_news.png"));
            JButton news = new JButton(newsIcon);
            news.setContentAreaFilled(false);
            news.setBorderPainted(false);
            news.setFocusPainted(false);
            news.setBounds(170,20,178,46);
            news.addActionListener(ActionListeners.openURL(this, "https://conquestreforged.com/news"));
            topLayers.add(news, 0);
            news.setRolloverEnabled(true);
            news.setRolloverIcon(new ImageIcon(RedditLauncher.class.getResource("/com/skcraft/launcher/button_news_hover.png")));

        //create bottom panel
        JPanel bottom = new JPanel();
        bottom.setOpaque(false);
        bottom.setLayout(new BorderLayout());
            bottom.add(getImage("/com/skcraft/launcher/bar_bottom.png", 1200, -1), Theme.headerAlignY); //create bottom bar


        //For root, sets layout and adds sub-panels
        root.setLayout(new BorderLayout());
        root.add(topLayers);
        root.add(bottom, BorderLayout.SOUTH);
        overlay.add(root);

        mediaPlayerComponent.getMediaPlayer().setOverlay(overlay);
        mediaPlayerComponent.getMediaPlayer().enableOverlay(true);

        mediaPlayer.setRepeat(true);
        mediaPlayer.playMedia("launcher-reddit/src/main/resources/com/skcraft/launcher/video.webm"); //change this when it's time for building?

        setIcons();
    }

    @Override
    public WebpagePanel createNewsPanel() {
        return WebpagePanel.forHTML("");
    }

    @Override
    public JButton createPrimaryButton(String name) {
        JButton button = new ColoredButton(name, Theme.primary, Theme.primaryAlt);
        button.setFont(new Font(button.getFont().getName(), Font.PLAIN, Theme.primarySize));
        button.setForeground(Theme.primaryText);
        button.setPreferredSize(Theme.primaryButtonSize);
        return button;
    }

    @Override
    protected JButton createSecondaryButton(String name) {
        JButton button = new ColoredButton(name, Theme.secondary, Theme.secondaryAlt);
        button.setFont(new Font(button.getFont().getName(), Font.PLAIN, Theme.secondarySize));
        button.setForeground(Theme.secondaryText);
        button.setPreferredSize(Theme.secondaryButtonSize);
        return button;
    }

    @Override
    protected JCheckBox createCheckBox(String name) {
        JCheckBox box = new JCheckBox(name);
        box.setFont(new Font(box.getFont().getName(), Font.PLAIN, Theme.secondarySize));
        box.setBackground(Theme.secondary);
        box.setForeground(Theme.secondaryText);
        box.setPreferredSize(Theme.secondaryButtonSize);
        box.setHorizontalAlignment(SwingConstants.CENTER);
        return box;
    }

    @Override
    protected void initComponents() {
        super.initComponents();
        getContentPane().removeAll();
        instancesTable.setSelectionBackground(Theme.primary);
        instancesTable.setSelectionForeground(Theme.primaryText);
        instancesTable.setForeground(Theme.secondaryText);
        instancesTable.setFont(new Font(instancesTable.getFont().getName(), Font.PLAIN, Theme.secondarySize));
        instancesTable.setOpaque(false);
    }

    private void setIcons() {
        Image mainIcon = SwingHelper.createImage(LauncherFrame.class, "/com/skcraft/launcher/icon.png");
        Image titleIcon = SwingHelper.createImage(LauncherFrame.class, "/com/skcraft/launcher/title.png");
        ArrayList<Image> icons = new ArrayList<Image>();
        if (mainIcon != null) {
            icons.add(mainIcon);
        }
        if (titleIcon != null) {
            icons.add(titleIcon.getScaledInstance(16, 16, Image.SCALE_SMOOTH));
        }
        setIconImages(icons);
    }

    private JLabel getImage(String filepath, int width, int height) {
        JLabel label = new JLabel();
        try {
            BufferedImage image = ImageIO.read(RedditLauncher.class.getResourceAsStream(filepath));
            label.setIcon(new ImageIcon(image.getScaledInstance(width, height, Image.SCALE_SMOOTH)));
            label.setHorizontalAlignment(Theme.headerAlignX);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return label;
    }
}
