package br.com.android.sample.view.cadastro;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.j256.ormlite.dao.Dao;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.util.Calendar;
import java.util.Date;

import br.com.android.sample.R;
import br.com.android.sample.domain.User;
import br.com.android.sample.domain.UserRole;
import br.com.android.sample.infrastructure.mask.Mask;
import br.com.android.sample.infrastructure.repository.RepositoryHelper;
import br.com.android.sample.view.mapa.MapsActivity_;
import eu.inmite.android.lib.validations.form.FormValidator;
import eu.inmite.android.lib.validations.form.annotations.MinLength;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;
import eu.inmite.android.lib.validations.form.callback.SimpleErrorPopupCallback;

@EActivity(R.layout.activity_cadastro_usuario)
public class CadastroUsuarioActivity extends AppCompatActivity {


    @OrmLiteDao(helper = RepositoryHelper.class)
    public Dao<User, Long> userRepository;

    @ViewById(R.id.cadastrar)
    Button buttonCadastrar;


    @ViewById(R.id.editTextNome)
    @NotEmpty(messageId = R.string.message_enterSeuNome)
    EditText nome;

    @ViewById(R.id.editTextEmail)
    @NotEmpty(messageId = R.string.message_enterYourEmail)
    EditText email;

    @ViewById(R.id.editTextData)
    @NotEmpty(messageId = R.string.message_enterDataNascimento)
    EditText data;

    @ViewById(R.id.editTextSenha)
    @NotEmpty(messageId = R.string.message_enterYourPassword)
    @MinLength(value = 4, messageId = R.string.message_yourPasswordLengthWrong)
    EditText editTextSenha;

    @ViewById(R.id.editTextConfirmarSenha)
    @NotEmpty(messageId = R.string.message_enterYourPassword)
    @MinLength(value = 4, messageId = R.string.message_yourPasswordLengthWrong)
    EditText editTextConfirmarSenha;

    @UiThread
    @Click(R.id.cadastrar)
    public void onCadastrarClick()
    {

        if ( !FormValidator.validate(this, new SimpleErrorPopupCallback(this)) ) return;

         if (this.editTextSenha.getText().toString().equals(this.editTextConfirmarSenha.getText().toString())) {
             this.cadastrar();
         }else{
             //Toast.makeText(this, R.string.message_enterSenhasNaoConferem, Toast.LENGTH_LONG);
             Snackbar.make(buttonCadastrar, R.string.message_enterSenhasNaoConferem, Snackbar.LENGTH_LONG).show();
         }

    }

    @Background
    public void cadastrar()
    {
        try
        {
            final User user = new User();
            user.setEmail( this.email.getText().toString() );
            user.setPassword(this.editTextSenha.getText().toString());
            user.setDisabled(false);
            user.setName(this.nome.getText().toString());
            user.setDataNacimento(new Date(this.data.getText().toString()));
            user.setRole(UserRole.USER);
            user.setCreated( Calendar.getInstance() );

            this.userRepository.createOrUpdate( user );

            super.startActivity( new Intent(this, MapsActivity_.class) );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            Snackbar.make(buttonCadastrar, R.string.message_errorNaoFoiPossivelCadastrar, Snackbar.LENGTH_LONG).show();
        }
    }

    @AfterViews
    public void onAfterViews() {
        data.addTextChangedListener(Mask.insert("##/##/####", data));
    }

}


/*
    @UiThread
    @Click(R.id.imageButton)
    public void onTirarBotaoClick(){
        abrirCamera();

    }

    public void abrirCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        InputStream stream = null;
        if(requestCode == 0 && resultCode == RESULT_OK){

            if (bitmap != null)
                bitmap.recycle();

            Bundle bundle = data != null ? data.getExtras() : null;
            bitmap = bundle != null ? (Bitmap) bundle.get("data") : null;
            img.setImageBitmap(resizeImage(this, bitmap, 100, 150));
        }

    }

    private static Bitmap resizeImage (Context context, Bitmap bmpOriginal, float newWidth, float newHeigth){
        Bitmap novoBmp = null;
        float temp;
        int w = bmpOriginal.getWidth();
        int h = bmpOriginal.getHeight();
        if (w > h) {
            temp = newWidth;
            newWidth = newHeigth;
            newHeigth = temp;
        }

        float densityFactor = context.getResources().getDisplayMetrics().density;
        float novoW = newWidth * densityFactor;
        float novoH = newHeigth * densityFactor;

        //Calcula escala em percentual do tamanho original para o novo tamanho
        float scalaW = novoW / w;
        float scalaH = novoH / h;

        //Criando uma matriz para manipulação da imagem bitmap
        Matrix matrix = new Matrix();

        //Definindo a proporção da escala para o matriz
        matrix.postScale(scalaW, scalaH);

        //Criando o novo Bitmap com o novo tamanho
        novoBmp = Bitmap.createBitmap(bmpOriginal, 0, 0, w, h,matrix, true);

        return novoBmp;
    }

    */
