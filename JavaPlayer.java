   import java.awt.Dimension;
   import java.awt.FlowLayout;
   import java.awt.Font;
   import java.awt.Point;
   import java.awt.event.ActionEvent;
   import java.awt.event.ActionListener;
   import java.awt.event.MouseEvent;
   import java.awt.event.MouseMotionListener;
   import java.io.BufferedReader;
   import java.io.File;
   import java.io.FileReader;
   import java.io.IOException;
   import java.net.MalformedURLException;
   import javax.media.ControllerEvent;
   import javax.media.ControllerListener;
   import javax.media.EndOfMediaEvent;
   import javax.media.Manager;
   import javax.media.MediaLocator;
   import javax.media.NoPlayerException;
   import javax.media.Player;
   import javax.media.PrefetchCompleteEvent;
   import javax.media.RealizeCompleteEvent;
   import javax.media.Time;
   import javax.swing.BorderFactory;
   import javax.swing.Box;
   import javax.swing.Icon;
   import javax.swing.ImageIcon;
   import javax.swing.JButton;
   import javax.swing.JFileChooser;
   import javax.swing.JOptionPane;
   import javax.swing.JPanel;
   import javax.swing.JProgressBar;
   import javax.swing.JWindow;
   import javax.swing.filechooser.FileFilter;
   import helliker.id3.ID3v2FormatException;
   import helliker.id3.PlaylistException;
   import helliker.id3.MP3File;
   import helliker.id3.Playlist;

   public class JavaPlayer extends JWindow implements ActionListener,ControllerListener,MouseMotionListener, Runnable {
      private Playlist playlist;
      private Player player = null;
      private JPanel mainPanel = null;
      private JProgressBar progressBar = null;
      private String fileTitle = "";
      private Thread playThread = null;
      public JavaPlayer() {
         super();
         mainPanel = new JPanel(new FlowLayout(FlowLayout.LEADING,1,1));
         mainPanel.setBorder(BorderFactory.createRaisedBevelBorder());
         mainPanel.addMouseMotionListener(this);
         Settings.loadSettings();
         setBounds(Settings.getMainWindowRect());
         buildToolbar();
         getContentPane().add(mainPanel);
         setVisible(true);
         playlist = new Playlist();
         File playlistFile = new File(Settings.getPlaylistDirectory(),	Settings.getPlaylistFile());
         if(playlistFile.exists() && playlistFile.isFile()) {
            progressBar.setString("Loading Playlist...");
            try {
               playlist.loadFromFile(playlistFile);
            }
               catch(IOException ex) {
                  errorMessage(ex.getMessage());
               }
               catch(PlaylistException ex) {
                  errorMessage(ex.getMessage());
               }
            progressBar.setString("");
         }
      }
      private void buildToolbar() {
         JButton button ;
         button = new JButton(new ImageIcon(getClass().getClassLoader().getResource("icons/Playlist16.gif")));
         button.setPreferredSize(new Dimension(20,20));
         button.setActionCommand("Open Playlist");
         button.addActionListener(this);
         mainPanel.add(button);
         button = new JButton(new ImageIcon(getClass().getClassLoader().getResource("icons/Manager16.gif")));
         button.setPreferredSize(new Dimension(20,20));
         button.setActionCommand("Playlist Manager");
         button.addActionListener(this);
         mainPanel.add(button);
         button = new JButton(new ImageIcon(getClass().getClassLoader().getResource("icons/Play16.gif")));
         button.setPreferredSize(new Dimension(20,20));
         button.setActionCommand("Play");
         button.addActionListener(this);
         mainPanel.add(button);
         button = new JButton(new ImageIcon(getClass().getClassLoader().getResource("icons/Pause16.gif")));
         button.setPreferredSize(new Dimension(20,20));
         button.setActionCommand("Pause");
         button.addActionListener(this);
         mainPanel.add(button);
         button = new JButton(new ImageIcon(getClass().getClassLoader().getResource("icons/Stop16.gif")));
         button.setPreferredSize(new Dimension(20,20));
         button.setActionCommand("Stop");
         button.addActionListener(this);
         mainPanel.add(button);
         button = new JButton(new ImageIcon(getClass().getClassLoader().getResource("icons/Previous16.gif")));
         button.setPreferredSize(new Dimension(20,20));
         button.setActionCommand("Previous");
         button.addActionListener(this);
         mainPanel.add(button);
         button = new JButton(new ImageIcon(getClass().getClassLoader().getResource("icons/Next16.gif")));
         button.setPreferredSize(new Dimension(20,20));
         button.setActionCommand("Next");
         button.addActionListener(this);
         mainPanel.add(button);
         mainPanel.add(Box.createHorizontalStrut(2));
         progressBar = new JProgressBar();
         progressBar.setPreferredSize(new Dimension(270,18));
         progressBar.setStringPainted(true);
         progressBar.setFont(new Font("Dialog",Font.BOLD,10));
         progressBar.setString("");
         mainPanel.add(progressBar);
         mainPanel.add(Box.createHorizontalStrut(2));
         button = new JButton(new ImageIcon(getClass().getClassLoader().getResource("icons/Exit16.gif")));
         button.setPreferredSize(new Dimension(20,20));
         button.setActionCommand("Exit");
         button.addActionListener(this);
         mainPanel.add(button);
      }
      public void errorMessage(String s) {
         JOptionPane.showMessageDialog(null, s, "Error", JOptionPane.ERROR_MESSAGE);
      }
      private void exitAction() {
         Settings.setMainWindowRect(getBounds());
         Settings.storeSettings();
         if(player != null) {
            player.removeControllerListener(this);
            player.stop();
            player.close();
            player = null;
         }
         if(playThread != null) {
            playThread = null;
         }
         System.exit(0);
      }
      private void managerAction() {
         if(playlist != null) {
            PlaylistManager manager = new PlaylistManager(this);
            manager.setVisible(true);
         }
      }
      private void playlistAction() {
         JFileChooser fileChooser = new JFileChooser(Settings.getPlaylistDirectory());
         fileChooser.setMultiSelectionEnabled(false);
         fileChooser.addChoosableFileFilter(new PlaylistFilter());
         if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            progressBar.setString("Loading Playlist...");
            playlist = new Playlist();
            try {
               File playlistFile = fileChooser.getSelectedFile();
               playlist.loadFromFile(playlistFile);
               Settings.setPlaylistDirectory(playlistFile.getParent());
               Settings.setPlaylistFile(playlistFile.getName());
            }
               catch(IOException ex) {
                  errorMessage(ex.getMessage());
               }
               catch(PlaylistException ex) {
                  errorMessage(ex.getMessage());
               }
            progressBar.setString("");
         }
      }
      public Playlist getPlaylist() {
         return playlist;
      }
      public void setPlaylist(Playlist p) {
         playlist = p;
      }
      public Player getPlayer() {
         return player;
      }
      public void play() {
         MP3File mp3File = null;
         int position = Settings.getPlaylistPosition();
         try {
            mp3File = (MP3File) playlist.get(position);
            fileTitle = mp3File.getTitle();
         }
            catch(ID3v2FormatException ex) {
               errorMessage(ex.getMessage());
            }
         if(player == null) {
            try {
               File file = new File(mp3File.getPath());
               MediaLocator mediaLocator = new MediaLocator(file.toURL());
               player = Manager.createPlayer(mediaLocator);
               player.addControllerListener(this);
               progressBar.setString("Realizing...");
               player.realize();
            }
               catch(MalformedURLException ex) {
                  errorMessage(ex.getMessage());
               }
               catch(NoPlayerException ex) {
                  errorMessage(ex.getMessage());
               }
               catch(IOException ex) {
                  errorMessage(ex.getMessage());
               }
         }
         else {
            player.start();
            progressBar.setString("Playing " + fileTitle);
         }
      }
   
      private void pause() {
         if(player != null) {
            MP3File mp3File = null;
            int position = Settings.getPlaylistPosition();
            try {
               mp3File = (MP3File) playlist.get(position);
               fileTitle = mp3File.getTitle();
               progressBar.setString( fileTitle + " Paused");
            }
               catch(ID3v2FormatException ex) {
                  errorMessage(ex.getMessage());
               }
            player.stop();
         }
      }
      public void stop() {
         if(player != null) {
            player.removeControllerListener(this);
            player.stop();
            player.close();
            player = null;
         }
         if(playThread != null) {
            playThread = null;
         }
         progressBar.setValue(0);
         progressBar.setString("");
      }
      private void previous() {
         int position = Settings.getPlaylistPosition();
         position--;
         if(position < 0)
            position = 0;
         Settings.setPlaylistPosition(position);
         if(player != null)
            stop();
         play();
      }
      private void next() {
         int position = Settings.getPlaylistPosition();
         position++;
         if(position >= playlist.size()) {
            position = 0;
            Settings.setPlaylistPosition(position);
            stop();
            return;
         }
         Settings.setPlaylistPosition(position);
         if(player != null)
            stop();
         play();
      }
      public static void main(String args[]) {
         new JavaPlayer();
      }
      public void actionPerformed(ActionEvent ev) {
         JButton button = (JButton) ev.getSource();
         String command = button.getActionCommand();
         if(command.equals("Exit")) 
            exitAction();
         else if(command.equals("Open Playlist"))
            playlistAction();
         else if(command.equals("Playlist Manager"))
            managerAction();
         else if(command.equals("Play"))
            play();
         else if(command.equals("Pause"))
            pause();
         else if(command.equals("Stop"))
            stop();
         else if(command.equals("Previous"))
            previous();
         else if(command.equals("Next"))
            next();
      }
      public void controllerUpdate(ControllerEvent ev) {
         if(ev instanceof RealizeCompleteEvent) {
            player.prefetch();
            progressBar.setString("Prefetching...");
         }
         if(ev instanceof PrefetchCompleteEvent) {
            Time time = player.getDuration();
            progressBar.setMaximum((int) time.getSeconds());
            progressBar.setString("Playing " + fileTitle);
            playThread = new Thread(this);
            playThread.start();
            player.getGainControl().setLevel(1);
            player.start();
         }
         if(ev instanceof EndOfMediaEvent) {
            player.removeControllerListener(this);
            player.stop();
            player.close();
            player = null;
            if(playThread != null) {
               playThread = null;
            }
            progressBar.setValue(0);
            next();
         }
      }
      public void mouseDragged(MouseEvent ev) {
         Point loc = getLocation();
         Point clk = ev.getPoint();
         Point pt = new Point(loc.x + clk.x,loc.y + clk.y);
         setLocation(pt);	
      }
      public void mouseMoved(MouseEvent ev) {}
      public void run() {
         while(playThread != null) {
            if(player != null) {
               Time time = player.getMediaTime();
               progressBar.setValue((int) time.getSeconds());
               try {
                  playThread.sleep(500);
               }
                  catch(InterruptedException ex) {}
            }
         }
      }
      class PlaylistFilter extends FileFilter{
         public boolean accept(File file) {
            if(file.isDirectory())
               return true;
            String s = file.getName().toLowerCase();
            if (s.endsWith(".m3u"))
               return true;
            return false;
         }
         public String getDescription() {
            return "Playlist Files";
         }
      	
      }
   }