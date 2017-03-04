package com.retroid.quiz.time.ultimate.trivia;

/**
 * Created by Aditya on 2/20/2017.
 */

public class UpdateDatabase {

    /*private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:D://retrospect.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void insert(String question, String correct_ans,
                       String wrong_ans1, String wrong_ans2,
                       String wrong_ans3, String category, int difficulty) {
        String sql = "INSERT INTO quiz(question ,correct_ans, wrong_ans1, wrong_ans2, wrong_ans3, category, difficulty) VALUES(?,?,?,?,?,?,?)";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, question);
            pstmt.setString(2, correct_ans);
            pstmt.setString(3, wrong_ans1);
            pstmt.setString(4, wrong_ans2);
            pstmt.setString(5, wrong_ans3);
            pstmt.setString(6, category);
            pstmt.setInt(7, difficulty);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {

        UpdateDatabase app = new UpdateDatabase();

        File folder = new File("D:/categories");
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                System.out.println(file.getName());
            }
        }*/




        /*JSONParser parser = new JSONParser();

        try {

            Object obj = parser.parse(new FileReader("/Users/<username>/Documents/file1.txt"));

            JSONObject jsonObject = (JSONObject) obj;

            String name = (String) jsonObject.get("Name");
            String author = (String) jsonObject.get("Author");
            JSONArray companyList = (JSONArray) jsonObject.get("Company List");

            Iterator<String> iterator = companyList.iterator();
            while (iterator.hasNext()) {
                System.out.println(iterator.next());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }*/

    // }

}
