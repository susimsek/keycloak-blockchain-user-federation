package org.keycloak.fabric.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Map;

//Standart Hata Objesi
//Jsonda döndürüleceği için getter setter lazım.@Data anatasyonu ile eklendi
//Jaksonuun özelliğini kullanarak null olmayanları jsonun içine ekledik.null ise değer jsonda gözükmeyecek
//@JsonView(Views.Base.class) ile responseda dönülecek fialdları işaretledik
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)//Sadece null değer içermeyenleri jsona ekle
@RequiredArgsConstructor
public class ApiError {

    @NonNull
    private int status;  //hata kodu

    @NonNull
    private String message; //hata mesaj

    @NonNull
    private String path; // hatanın hangi urlde

    @NonNull
    private long timestamp; //hatanın zamanı

    private Map<String, String> errors;


}
