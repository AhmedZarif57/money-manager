@Override
public void start(Stage stage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
    Scene scene = new Scene(root);

    scene.getStylesheets().addAll(
            getClass().getResource("/css/app.css").toExternalForm(),
            getClass().getResource("/css/dashboard.css").toExternalForm()
    );

    stage.setScene(scene);
    stage.show();
}
