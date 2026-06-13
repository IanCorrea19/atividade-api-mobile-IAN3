package com.example.atividade_api_mobile_ian3
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Conecta este código à tela XML que criamos
        setContentView(R.layout.activity_main)

        // 1. Mapeando os elementos da tela (IDs do XML)
        val etBookTitle = findViewById<EditText>(R.id.etBookTitle)
        val btnSearch = findViewById<Button>(R.id.btnSearch)
        val tvAuthor = findViewById<TextView>(R.id.tvAuthor)
        val tvYear = findViewById<TextView>(R.id.tvYear)
        val tvLanguages = findViewById<TextView>(R.id.tvLanguages)

        // 2. Configurando a ação do botão de busca
        // 2. Configurando a ação do botão de busca
        btnSearch.setOnClickListener {
            val title = etBookTitle.text.toString().trim()

            // 3. Validação de campo vazio
            if (title.isEmpty()) {
                Toast.makeText(this, "Por favor, digite o título de um livro!", Toast.LENGTH_SHORT)
                    .show()
            } else {
                // Informa o usuário que a busca começou
                Toast.makeText(this, "Buscando...", Toast.LENGTH_SHORT).show()

                // 4. Montando a URL da API (Trocamos espaços por '+' para formato de link)
                val url = "https://openlibrary.org/search.json?q=${title.replace(" ", "+")}"

                // 5. Configurando a requisição com Volley
                val queue = com.android.volley.toolbox.Volley.newRequestQueue(this)

                val request = com.android.volley.toolbox.JsonObjectRequest(
                    com.android.volley.Request.Method.GET, url, null,
                    { response ->
                        // 6. Sucesso! Lendo o JSON que a API retornou
                        try {
                            val docs = response.getJSONArray("docs")

                            if (docs.length() > 0) {
                                // Pega o primeiro livro da lista de resultados
                                val firstBook = docs.getJSONObject(0)

                                // Extrai as 3 informações úteis exigidas
                                val authors = firstBook.optJSONArray("author_name")
                                val author =
                                    if (authors != null && authors.length() > 0) authors.getString(0) else "Não informado"

                                val year = firstBook.optInt("first_publish_year", 0)
                                val yearStr = if (year > 0) year.toString() else "Não informado"

                                val languages = firstBook.optJSONArray("language")
                                val langCount = languages?.length() ?: 0

                                // 7. Exibe na tela do aplicativo
                                tvAuthor.text = "Autor: $author"
                                tvYear.text = "Ano de Publicação: $yearStr"
                                tvLanguages.text = "Idiomas Disponíveis: $langCount detectados"

                            } else {
                                Toast.makeText(
                                    this,
                                    "Nenhum livro encontrado com esse título.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(
                                this,
                                "Erro ao interpretar os dados da API.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    { error ->
                        // 8. Tratamento de Erro de Rede (Requisito da atividade)
                        Toast.makeText(
                            this,
                            "Erro de conexão! Verifique sua internet.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                )

                // Dispara a requisição
                queue.add(request)
            }
        }
    }
}