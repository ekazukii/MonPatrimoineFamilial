package fr.cytech.mpf.service.validation;

public class ValidationResponse {

    private boolean sucess;
    private String errorMessage;
    
    public void setError(String errorMessage) {
        this.errorMessage = errorMessage;
        this.sucess = false;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setSuccess(){
        this.sucess = true;
    }

    public boolean isSucess() {
        return sucess;
    }

}