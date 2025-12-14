import java.sql.*;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/booking_db";
        String username = "root";
        String pass = "";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, username, pass);
            // Types of Statement:
            // Statement :
            Statement stm = con.createStatement();
            String query  = "SELECT * FROM booking_data";
            ResultSet rs = stm.executeQuery(query);

            System.out.println("Bookings Data : ");

            while (rs.next()) {
                System.out.println("Client ID: "+rs.getInt("CLIENT_ID"));
                System.out.println("Client Full Name: "+rs.getString("FIRST_NAME")+" "+rs.getString("LAST_NAME"));
                System.out.println("+--------------------------------------+");
            }

            System.out.println(" This is the Prepared Statement ...... ");

            // Prepared Statement :
            String prepQuery = "SELECT * FROM booking_data WHERE CLIENT_ID = ? ";
            PreparedStatement prepStm = con.prepareStatement(prepQuery);
            prepStm.setInt(1, 1);

            ResultSet prepRs = prepStm.executeQuery();
            while (prepRs.next()) {
                System.out.println("Client ID: "+prepRs.getInt("CLIENT_ID"));
                System.out.println("Client Full Name: "+prepRs.getString("FIRST_NAME")+" "+prepRs.getString("LAST_NAME"));
                System.out.println("+--------------------------------------+");
            }

            System.out.println(" This is the Callable Statement ...... ");

            // Callable Statement :
            CallableStatement callStm = con.prepareCall("{CALL PS_GetClients()}");

            ResultSet callRs = callStm.executeQuery();
            while (callRs.next()) {
                System.out.println("Client ID: "+callRs.getInt("CLIENT_ID"));
                System.out.println("Client Full Name: "+callRs.getString("FIRST_NAME")+" "+callRs.getString("LAST_NAME"));
                System.out.println("+--------------------------------------+");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
