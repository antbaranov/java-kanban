package constants;

public enum StatusCode {

        CODE_200(200),
    CODE_400(400),
    CODE_405(405),
    CODE_403(403),
    CODE_404(404),
    CODE_201(201);
        private int code;
    StatusCode (int code){
        this.code = code;
    }
        public int getCode(){ return code;}

}
