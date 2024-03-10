package com.example.bookhaul.screens.login

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.textInputServiceFactory
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.bookhaul.navigation.BookHaulScreens
import com.example.bookhaul.screens.splashscreen.BookLogo

@Composable
fun BookLoginScreen(navController: NavController,
                    viewModel: LoginScreenViewModel= androidx.lifecycle.viewmodel.compose.viewModel()) {
    val showLoginForm = rememberSaveable{ mutableStateOf(true)}
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top) {
            BookLogo()
            if(showLoginForm.value) UserForm(loading = false,isAccountCreated = false){email,pwd ->
                viewModel.signInUserWithEmailAndPassword(email,pwd){
                            navController.navigate(BookHaulScreens.BookHomeScreen.name)
                }
            }
            else{
                UserForm(loading = false,isAccountCreated = true){ email,pwd ->
                     viewModel.createUserWithEmailAndPassword(email,pwd) {
                         navController.navigate(BookHaulScreens.BookHomeScreen.name)
                     }
                }
            }
        }
        Spacer(modifier = Modifier.height(15.dp))
        Row(modifier = Modifier.padding(15.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            val text = if(showLoginForm.value) "Sign up" else "Login"
            Text(text = "New User?")
            Text(text = text, modifier = Modifier
                .clickable {
                    showLoginForm.value = !showLoginForm.value
                }
                .padding(start = 5.dp), fontWeight = FontWeight.Bold,color=MaterialTheme.colors.secondaryVariant)
        }

    }

}

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UserForm(
    loading:Boolean=false,
    isAccountCreated:Boolean=false,
    onDone:(String,String) -> Unit = {email,pwd ->},
){
    val email= rememberSaveable { mutableStateOf("")}
    val password = rememberSaveable{ mutableStateOf("")}
    val passwordVisibility= rememberSaveable{ mutableStateOf(false)}
    val passwordFocusRequest= FocusRequester.Default
    val keyboardController =LocalSoftwareKeyboardController.current
    val valid= remember(email.value,password.value){
        email.value.trim().isNotEmpty() && password.value.trim().isNotEmpty()
    }

    val modifier= Modifier
        .height(250.dp)
        .background(color = MaterialTheme.colors.background)
        .verticalScroll(rememberScrollState())
       Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
           if(isAccountCreated) Text(text = "Please Enter a valid email and password having 8 minimum characters",
           modifier = Modifier.padding(4.dp)) else Text(text = "")
           emailInput(emailState = email,
               enabled = !loading,
               onAction = KeyboardActions { passwordFocusRequest.requestFocus() })
    PasswordInputField(modifier=Modifier.focusRequester(passwordFocusRequest),
    passwordState=password,labelId="Password",enabled=!loading,passwordVisibility=passwordVisibility,
    onAction= KeyboardActions {
        if(!valid) return@KeyboardActions
        onDone(email.value.trim(),password.value.trim())
    })
       SubmitButton(
           textId= if(isAccountCreated) "Create Account" else "Login",loading=loading,validInputs=valid){
           onDone(email.value.trim(),password.value.trim())
           keyboardController!!.hide()
       }

       }
}

@Composable
fun SubmitButton(textId: String, loading: Boolean, validInputs: Boolean,onClick: () -> Unit) {
      Button(onClick = onClick,
          modifier = Modifier
              .padding(3.dp)
              .fillMaxWidth(),
          enabled = !loading && validInputs, shape = CircleShape) {
               if(loading) CircularProgressIndicator(modifier = Modifier.size(25.dp))
          else Text(text = textId, modifier = Modifier.padding(5.dp))
      }
}

@Composable
fun PasswordInputField(modifier: Modifier,
                       passwordState: MutableState<String>,
                       labelId: String, enabled: Boolean,imeAction: ImeAction=ImeAction.Done,
                       passwordVisibility: MutableState<Boolean>,
                       onAction: KeyboardActions= KeyboardActions.Default) {
    val visualTransformation= if(passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation()
    OutlinedTextField(value = passwordState.value, onValueChange = {passwordState.value=it}, label = { Text(
        text = labelId)}, singleLine = true,
        textStyle = TextStyle(fontSize = 18.sp,
            color = MaterialTheme.colors.onBackground),
        modifier = modifier
            .padding(start = 10.dp, bottom = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        enabled=enabled, keyboardOptions = KeyboardOptions(keyboardType =KeyboardType.Password,imeAction=imeAction),
    visualTransformation = visualTransformation, trailingIcon = {
        PasswordVisibility(passwordVisibility=passwordVisibility)
        }, keyboardActions = onAction)

}


@SuppressLint("SuspiciousIndentation")
@Composable
fun PasswordVisibility(passwordVisibility: MutableState<Boolean>) {
  val visible =passwordVisibility.value
    IconButton(onClick = {passwordVisibility.value =!visible }) {
        Icons.Default.Close
    }
}

@Composable
fun emailInput(modifier: Modifier=Modifier,
               emailState:MutableState<String>,labelId:String="Email",
               enabled:Boolean=true,imeAction:ImeAction= ImeAction.Next,
               onAction: KeyboardActions=KeyboardActions.Default){
                InputField(modifier=modifier, valueState = emailState,
                    enabled = enabled, labelId = labelId, onAction = onAction,
                    imeAction = imeAction, keyBoardType = KeyboardType.Email)
}

@Composable
fun InputField(
    modifier: Modifier=Modifier,
    valueState:MutableState<String>,labelId:String="Email",
    enabled:Boolean=true,
    isSingleLine:Boolean=true,imeAction:ImeAction= ImeAction.Next,keyBoardType:KeyboardType=KeyboardType.Text,
    onAction: KeyboardActions=KeyboardActions.Default) {
    
    OutlinedTextField(value = valueState.value, onValueChange ={valueState.value=it},label={ Text(
        text = labelId)}, singleLine = isSingleLine,
        textStyle = TextStyle(fontSize = 18.sp,
            color = MaterialTheme.colors.onBackground),
        modifier = modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),enabled=enabled,
        keyboardOptions = KeyboardOptions(keyboardType = keyBoardType, imeAction = imeAction), keyboardActions = onAction )

}
