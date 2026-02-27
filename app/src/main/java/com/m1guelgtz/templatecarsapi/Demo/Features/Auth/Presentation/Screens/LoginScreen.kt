package com.m1guelgtz.templatecarsapi.Demo.Features.Auth.Presentation.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.m1guelgtz.templatecarsapi.Demo.Core.Ui.theme.*
import com.m1guelgtz.templatecarsapi.Demo.Features.Auth.Presentation.ViewModels.AuthState
import com.m1guelgtz.templatecarsapi.Demo.Features.Auth.Presentation.ViewModels.AuthViewModel

@Composable
fun FieldLabel(label: String) {
    Text(
        text = label.uppercase(),
        color = OnSurfaceVariant,
        fontSize = 12.sp,
        fontWeight = FontWeight.W500,
        letterSpacing = 0.05.sp,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingContent: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = SurfaceContainer,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.weight(1f)) {
            if (value.isEmpty()) {
                Text(
                    text = placeholder,
                    color = Outline,
                    fontSize = 15.sp
                )
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = TextStyle(
                    color = OnSurface,
                    fontSize = 15.sp
                ),
                cursorBrush = SolidColor(Primary),
                singleLine = true,
                visualTransformation = visualTransformation,
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (trailingContent != null) {
            Spacer(modifier = Modifier.width(8.dp))
            trailingContent()
        }
    }
}

@Composable
fun PrimaryButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(100.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Primary,
            contentColor = OnPrimary,
            disabledContainerColor = SurfaceHigh,
            disabledContentColor = OnSurfaceVariant
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
    ) {
        Text(
            text = label,
            fontSize = 15.sp,
            fontWeight = FontWeight.W500,
            letterSpacing = 0.02.sp
        )
    }
}

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val authState by viewModel.authState.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            onLoginSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(rememberScrollState())
    ) {
        // Brand Hero
        Column(
            modifier = Modifier
                .padding(start = 28.dp, end = 28.dp, top = 40.dp, bottom = 32.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(PrimaryContainer, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("⚡", fontSize = 26.sp)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "DevSync",
                color = OnSurface,
                fontSize = 28.sp,
                fontWeight = FontWeight.W500,
                letterSpacing = (-0.3).sp
            )
            Text(
                text = "Agile project management for teams",
                color = OnSurfaceVariant,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(
                    Surface,
                    RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
                )
                .padding(28.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Tab Switcher
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Background, RoundedCornerShape(14.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                listOf("login" to "Sign In", "register" to "Register").forEach { (key, label) ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(
                                if (uiState.activeTab == key) PrimaryContainer else Color.Transparent,
                                RoundedCornerShape(10.dp)
                            )
                            .clickable { viewModel.onTabChanged(key) }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            color = if (uiState.activeTab == key) Primary else OnSurfaceVariant,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W500,
                            letterSpacing = 0.02.sp
                        )
                    }
                }
            }

            if (uiState.activeTab == "login") {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Column {
                        FieldLabel("Email")
                        AppTextField(
                            value = uiState.loginEmail,
                            onValueChange = { viewModel.onLoginEmailChanged(it) },
                            placeholder = "your@email.com"
                        )
                    }
                    Column {
                        FieldLabel("Password")
                        AppTextField(
                            value = uiState.loginPassword,
                            onValueChange = { viewModel.onLoginPasswordChanged(it) },
                            placeholder = "Enter your password",
                            visualTransformation = if (uiState.showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingContent = {
                                IconButton(
                                    onClick = { viewModel.onShowPasswordToggled() },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = if (uiState.showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                        contentDescription = "Toggle password",
                                        tint = OnSurfaceVariant,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if (authState is AuthState.Loading) {
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = Primary)
                        }
                    } else {
                        PrimaryButton(
                            label = "Sign In",
                            onClick = {
                                viewModel.login()
                            }
                        )
                    }

                    if (authState is AuthState.Error) {
                        Text(
                            text = (authState as AuthState.Error).message,
                            color = ErrorColor,
                            fontSize = 12.sp,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }

                    Text(
                        text = "Use alex@devsync.io to sign in with demo data",
                        color = OnSurfaceVariant,
                        fontSize = 12.sp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                 Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Column {
                        FieldLabel("Username")
                        AppTextField(
                            value = uiState.regUsername,
                            onValueChange = { viewModel.onRegUsernameChanged(it) },
                            placeholder = "Choose a username"
                        )
                    }
                    Column {
                        FieldLabel("Email")
                        AppTextField(
                            value = uiState.regEmail,
                            onValueChange = { viewModel.onRegEmailChanged(it) },
                            placeholder = "your@email.com"
                        )
                    }
                    Column {
                        FieldLabel("Password")
                        AppTextField(
                            value = uiState.regPassword,
                            onValueChange = { viewModel.onRegPasswordChanged(it) },
                            placeholder = "Create a strong password",
                            visualTransformation = if (uiState.showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingContent = {
                                IconButton(
                                    onClick = { viewModel.onShowPasswordToggled() },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = if (uiState.showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                        contentDescription = "Toggle password",
                                        tint = OnSurfaceVariant,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        )
                    }

                    if (authState is AuthState.Loading) {
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = Primary)
                        }
                    } else {
                        PrimaryButton(
                            label = "Create Account",
                            enabled = uiState.regUsername.isNotBlank() && uiState.regEmail.isNotBlank() && uiState.regPassword.isNotBlank(),
                            onClick = {
                                viewModel.register()
                            }
                        )
                    }

                    if (authState is AuthState.Error) {
                        Text(
                            text = (authState as AuthState.Error).message,
                            color = ErrorColor,
                            fontSize = 12.sp,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}
