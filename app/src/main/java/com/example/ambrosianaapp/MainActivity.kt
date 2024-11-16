package com.example.ambrosianaapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.ambrosianaapp.ui.theme.AmbrosianaAppTheme


import com.amplifyframework.ui.authenticator.ui.Authenticator
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.api.graphql.model.ModelSubscription
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.Todo


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AmbrosianaAppTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background)
                {
                    Authenticator { state ->
                        Column {
                            Text(
                                text = "Hello ${state.user.username}!",
                            )

                            Button(onClick = {
                                val todo = Todo.builder()
                                    .content("My first todo")
                                    .build()


                                Amplify.API.mutate(
                                    ModelMutation.create(todo),
                                    { Log.i("MyAmplifyApp", "Added Todo with id: ${it.data.id}")},
                                    { Log.e("MyAmplifyApp", "Create failed", it)},
                                )
                            }) {
                                Text(text = "Create Todo")
                            }
                            Button(onClick = {
                                Amplify.Auth.signOut {  }
                            }) {
                                Text(text = "Sign Out")
                            }
                            TodoList()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AmbrosianaAppTheme {
        Greeting("Android")
    }
}

@Composable
fun TodoList() {
    var todoList by remember {mutableStateOf(emptyList<Todo>())}

    LaunchedEffect(Unit) {
        Amplify.API.query(
            ModelQuery.list(Todo::class.java),
            { todoList = it.data.items.toList() },
            { Log.e("MyAmplifyApp", "Failed to query.", it) }
        )

        Amplify.API.subscribe(
            ModelSubscription.onCreate(Todo::class.java),
            { Log.i("ApiQuickStart", "Subscription established") },
            {
                Log.i("ApiQuickStart", "Todo create subscription received: ${it.data}")
                todoList = todoList + it.data
            },
            { Log.e("ApiQuickStart", "Subscription failed", it) },
            { Log.i("ApiQuickStart", "Subscription completed") }
        )
    }
    LazyColumn {
        items(todoList) {
            todo -> Row{
                Text(text = todo.content)
        }
        }
    }
}