public class Main {
    public static void main(String[] args) {
        // invokeLater() its a safe way for modifing the swing form by creating an other thread.
        // Lambda Expression ()-> version 7 and up
        javax.swing.SwingUtilities.invokeLater(()->new LoginForm().setVisible(true));
    }
}