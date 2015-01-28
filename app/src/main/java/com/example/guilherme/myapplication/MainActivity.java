package com.example.guilherme.myapplication;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jogo.pastordeovelhas.Casa;
import com.jogo.pastordeovelhas.Dinamica;
import com.jogo.pastordeovelhas.Figurante;
import com.jogo.pastordeovelhas.Jogador;
import com.jogo.pastordeovelhas.Lobo;
import com.jogo.pastordeovelhas.Ocupante;
import com.jogo.pastordeovelhas.Ovelha;
import com.jogo.pastordeovelhas.Pastor;
import com.jogo.pastordeovelhas.Personagem;

import java.util.ArrayList;
import java.util.Iterator;


public class MainActivity extends ActionBarActivity {

    private Button[][] buttons;
    private int ids[][] = {{R.id.button21,R.id.button22,R.id.button23,
            R.id.button24,R.id.button25},{R.id.button16,R.id.button17,
            R.id.button18,R.id.button19,R.id.button20},{R.id.button11,
            R.id.button12,R.id.button13,R.id.button14,R.id.button15},{
            R.id.button6,R.id.button7,R.id.button8,R.id.button9,R.id.button10}
            ,{R.id.button1,R.id.button2,R.id.button3,R.id.button4,R.id.button5}};

    private TextView texto;
    private Button buttonMover,buttonOvelha,buttonCerca;
    private Dinamica dinamicaJogo;
    private Boolean gameOn;

    /**
     * Jogador do apk.
     */
    private Jogador meuJogador;
    /**
     * Grupo de adversarios.
     */
    private Jogador[] adversarios;
    /**
     * Casas para o uso dos botoes.
     */
    private Casa ultimaCasaPressionada,casaAcao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        texto = (TextView) findViewById(R.id.text);
        buttons = new Button[5][5];
        generatorButton();
        gameOn = false;

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.newGame) {
            startNewGame();
        }
        else{
            MainActivity.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private class ButtonMoveListener implements View.OnClickListener{
        private int x,y;
        private Casa casa;
        public ButtonMoveListener(int x, int y){
            texto.setText("OPA");
            casa = ultimaCasaPressionada;
            if (x >= 0) {
                this.x = x;
                this.y = y;
                casaAcao = new Casa(x,y);
            }else {
                this.x = x;
                this.y = y;
            }
        }

        @Override
        public void onClick(View view) {
            texto.setText("Declarando");
            if (gameOn){
                if (meuJogador.casaPertenceAoJogador(casa) && x >= 0 ){
                    //Precisamos saber qual personagem eh
                    switch (dinamicaJogo.casaOcupadaPor(casa)) {
                        case Pastor:
                            texto.setText("ACAO:");
                            buttonMover.setVisibility(View.VISIBLE);
                            buttonMover.setOnClickListener(new ButtonMoveListener(-1, 0));
                            buttonMover.setText("Mover");
                            buttonOvelha.setVisibility(View.VISIBLE);
                            buttonOvelha.setOnClickListener(new ButtonMoveListener(-1, 1));
                            buttonOvelha.setText("Criar ovelha");
                            buttonCerca.setVisibility(View.VISIBLE);
                            buttonCerca.setOnClickListener(new ButtonMoveListener(-1, 2));
                            buttonCerca.setText("Cercar");
                            break;
                        case Lobo:
                            ultimaCasaPressionada = this.casa;
                            texto.setText("ACAO:");
                            buttonMover.setVisibility(View.VISIBLE);
                            buttonMover.setText("Mover");
                            buttonMover.setOnClickListener(new ButtonMoveListener(-1,0));
                            break;
                    }
                }
                else if (meuJogador.casaPertenceAoJogador(casa) && x < 0 ){
                    desativarCasasDisponiveis(ultimaCasaPressionada);
                    desativaCercaAoRedor(ultimaCasaPressionada);
                    desativaOvelhaAoRedor(ultimaCasaPressionada);
                    switch(y){
                        case 0:
                            habilitarMovimentoPersonagem();
                            break;
                        case 1:
                            if (dinamicaJogo.casaOcupadaPor(ultimaCasaPressionada).equals(Ocupante.Pastor)){
                                criarOvelha();
                            }
                            break;
                        case 2:
                            if (dinamicaJogo.casaOcupadaPor(ultimaCasaPressionada).equals(Ocupante.Pastor)){
                                criarCerca();
                            }
                            break;
                    }
                }
            }
        }
    }
    private class ButtonClickListener implements View.OnClickListener{
        private int x,y;
        private Casa casa;
        public ButtonClickListener(int x, int y){
            this.x = x;
            this.y = y;
            casa = new Casa(x, y);
        }

        @Override
        public void onClick(View view) {
             if (gameOn){
                if (meuJogador.casaPertenceAoJogador(casa) ){
                    //Precisamos saber qual personagem eh

                    switch (dinamicaJogo.casaOcupadaPor(casa)) {
                        case Pastor:
                            ultimaCasaPressionada = this.casa;
                            avaliaCasaAoRedor(this.casa);
                            break;
                        case Lobo:
                            ultimaCasaPressionada = this.casa;
                            avaliaCasaAoRedor(this.casa);
                            avaliaCercaAoRedor(this.casa);
                            break;
                        default:
                            break;
                      }

                }
            }

        }

    }

    private void generatorButton(){
        int i=0;
        int j=0;
        for (i=0;i<5;i++){
            for (j=0;j<5;j++){
                buttons[i][j]=(Button) findViewById(ids[i][j]);
                buttons[i][j].setOnClickListener(new ButtonClickListener(i,j));
            }
        }
        buttonMover = (Button) findViewById(R.id.button37);
        buttonMover.setVisibility(View.GONE);
        buttonMover.setOnClickListener(new ButtonClickListener(-1,0));
        buttonOvelha = (Button) findViewById(R.id.button38);
        buttonOvelha.setVisibility(View.GONE);
        buttonOvelha.setOnClickListener(new ButtonClickListener(-1,1));
        buttonCerca = (Button) findViewById(R.id.button39);
        buttonCerca.setVisibility(View.GONE);
        buttonCerca.setOnClickListener(new ButtonClickListener(-1,2));
    }
    private void carregarFundoVazioTodas(){
        int i=0;
        int j=0;
        for (i=0;i<5;i++){
            for (j=0;j<5;j++) buttons[i][j].setBackgroundResource(R.drawable.vazio);
        }
    }
    public void startNewGame(){
        carregarFundoVazioTodas();
        dinamicaJogo = new Dinamica(25);
        gameOn=true;
        //->OBTER POSICAO DAS PECAS ALOCAR SEMPRE A ESQUERDA DA CASA
        Casa casaAlocarEsquerda = new Casa(0,0);
        Pastor meuPastor = new Pastor(Ocupante.Pastor,casaAlocarEsquerda);
        Casa casaLobo = new Casa(0,1);
        Lobo meuLobo = new Lobo(Ocupante.Lobo,casaLobo);

        meuJogador = new Jogador(new ArrayList<Personagem>());
        meuJogador.addPersonagem(meuPastor);
        meuJogador.addPersonagem(meuLobo);
        // Carregar posicao dos elementos no jogo
        atualizarCasaPorOcupante(meuPastor.getCasaPersonagem(),meuPastor.getNomeEspaco());
        atualizarCasaPorOcupante(meuLobo.getCasaPersonagem(),meuLobo.getNomeEspaco());
    }
    private Boolean atualizarCasaPorOcupante(Casa casa, Ocupante ocupante){
        switch (ocupante){
            case Ovelha:
                buttons[casa.getPosicaoX()][casa.getPosicaoY()].setBackgroundResource(R.drawable.sheep);
                return dinamicaJogo.ocuparCasa(casa,ocupante);
            case Pastor:
                buttons[casa.getPosicaoX()][casa.getPosicaoY()].setBackgroundResource(R.drawable.pastor);
                return dinamicaJogo.ocuparCasa(casa,ocupante);
            case Lobo:
                buttons[casa.getPosicaoX()][casa.getPosicaoY()].setBackgroundResource(R.drawable.wolf);
                return dinamicaJogo.ocuparCasa(casa,ocupante);
            case Cerca:
                buttons[casa.getPosicaoX()][casa.getPosicaoY()].setBackgroundResource(R.drawable.wall);
                return dinamicaJogo.ocuparCasa(casa,ocupante);
            case Vazio:
            default:
                buttons[casa.getPosicaoX()][casa.getPosicaoY()].setBackgroundResource(R.drawable.vazio);
                dinamicaJogo.esvaziarCasa(casa);
                return true;
        }
    }

    /**
     * Metodos complementares p/ os botoes.
     */


    public void alteraButao(int x,int y){
        buttons[x][y].setOnClickListener(new ButtonMoveListener(x,y));
    }

    public void desalteraButao(int x,int y){
        buttons[x][y].setOnClickListener(new ButtonClickListener(x,y));
    }
    public void alteraButaoOvelha(int x,int y){
        //   buttons[x][y].setOnClickListener(new ButtonOvelhaCercaListener(x,y));
    }

    public void desalteraButaoOvelha(int x,int y){
        buttons[x][y].setOnClickListener(new ButtonClickListener(x,y));
    }
    public void alteraButaoCerca(int x,int y){
     //   buttons[x][y].setOnClickListener(new ButtonOvelhaCercaListener(x,y));
    }

    public void desalteraButaoDaCerca(int x,int y){
        buttons[x][y].setOnClickListener(new ButtonClickListener(x,y));
    }

    /**
     * Avalia ocupantes das casas.
     */
    public void avaliaCasaAoRedor(Casa casa){
        int x = casa.getPosicaoX();
        int y = casa.getPosicaoY();
        int i = 0;
        Casa casaX;
        //Casa de cima vazia

        if (x+1 <5){
             casaX = new Casa(x+1,y);
            if (dinamicaJogo.espacoDisponivel(casaX)){
                alteraButao(x+1,y);
                i++;
            } else if (dinamicaJogo.casaOcupadaPor(casaX).equals(Ocupante.Ovelha)){
                alteraButaoOvelha(x + 1, y);
            }
        }
        if (x-1 >=0){
            casaX = new Casa(x-1,y);

            if (dinamicaJogo.espacoDisponivel(casaX)){
                alteraButao(x-1,y);
                i++;
            } else if (dinamicaJogo.casaOcupadaPor(casaX).equals(Ocupante.Ovelha)){
                alteraButaoOvelha(x - 1, y);
            }
        }
        if (y+1 <5){
            casaX = new Casa(x,y+1);

            if (dinamicaJogo.espacoDisponivel(casaX)){
                alteraButao(x,y+1);
                i++;
            }else if (dinamicaJogo.casaOcupadaPor(casaX).equals(Ocupante.Ovelha)){
                alteraButaoOvelha(x, y + 1);
            }
        }
        if (y-1 >= 0){
            casaX = new Casa(x,y-1);

            if (dinamicaJogo.espacoDisponivel(casaX)){
                alteraButao(x,y-1);
                i++;
            }else if (dinamicaJogo.casaOcupadaPor(casaX).equals(Ocupante.Ovelha)){
                alteraButaoOvelha(x, y - 1);
            }
        }
    }
    public void avaliaCercaAoRedor(Casa casa){
        int x = casa.getPosicaoX();
        int y = casa.getPosicaoY();
        //Casa de cima vazia
        if (x+1 <5){
            if (dinamicaJogo.casaOcupadaPor(new Casa(x+1,y)).equals(Ocupante.Cerca)){
                alteraButaoCerca(x + 1, y);
            }
        }
        if (x-1 >=0){
            if (dinamicaJogo.casaOcupadaPor(new Casa(x-1,y)).equals(Ocupante.Cerca)){
                alteraButaoCerca(x - 1, y);
            }
        }
        if (y+1 <5){
            if (dinamicaJogo.casaOcupadaPor(new Casa(x,y+1)).equals(Ocupante.Cerca)){
                alteraButaoCerca(x, y + 1);
            }
        }
        if (y-1 >= 0){
            if (dinamicaJogo.casaOcupadaPor(new Casa(x,y-1)).equals(Ocupante.Cerca)){
                alteraButaoCerca(x, y - 1);
            }
        }
    }
    public void desativaOvelhaAoRedor(Casa casa){
        int x = casa.getPosicaoX();
        int y = casa.getPosicaoY();
        //Casa de cima vazia
        if (x+1 < 5){
            if (dinamicaJogo.casaOcupadaPor(new Casa(x+1,y)).equals(Ocupante.Ovelha)){
                desalteraButaoOvelha(x + 1, y);
            }
        }
        if (x-1 >= 0){
            if (dinamicaJogo.casaOcupadaPor(new Casa(x-1,y)).equals(Ocupante.Ovelha)){
                desalteraButaoOvelha(x - 1, y);
            }
        }
        if (y+1 < 5){
            if (dinamicaJogo.casaOcupadaPor(new Casa(x,y+1)).equals(Ocupante.Ovelha)){
                desalteraButaoOvelha(x, y + 1);
            }
        }
        if (y-1 >= 0){
            if (dinamicaJogo.casaOcupadaPor(new Casa(x,y-1)).equals(Ocupante.Ovelha)){
                desalteraButaoOvelha(x, y - 1);
            }
        }
    }
    public void desativaCercaAoRedor(Casa casa){
        int x = casa.getPosicaoX();
        int y = casa.getPosicaoY();
        //Casa de cima vazia
        if (x+1 <5){
            if (dinamicaJogo.casaOcupadaPor(new Casa(x+1,y)).equals(Ocupante.Cerca)){
                desalteraButaoDaCerca(x + 1, y);
            }
        }
        if (x-1 >=0){
            if (dinamicaJogo.casaOcupadaPor(new Casa(x-1,y)).equals(Ocupante.Cerca)){
                desalteraButaoDaCerca(x - 1, y);
            }
        }
        if (y+1 <5){
            if (dinamicaJogo.casaOcupadaPor(new Casa(x,y+1)).equals(Ocupante.Cerca)){
                desalteraButaoDaCerca(x, y + 1);
            }
        }
        if (y-1 >= 0){
            if (dinamicaJogo.casaOcupadaPor(new Casa(x,y-1)).equals(Ocupante.Cerca)){
                desalteraButaoDaCerca(x, y - 1);
            }
        }
    }

    public void desativarCasasDisponiveis(Casa casa){
        int x = casa.getPosicaoX();
        int y = casa.getPosicaoY();
        //Casa de cima vazia
        if (x+1 <5){
            if (dinamicaJogo.espacoDisponivel(new Casa(x+1,y))){
                desalteraButao(x + 1, y);
            }
        }
        if (x-1 >=0){
            if (dinamicaJogo.espacoDisponivel(new Casa(x-1,y))){
                desalteraButao(x - 1, y);
            }
        }
        if (y+1 <5){
            if (dinamicaJogo.espacoDisponivel(new Casa(x,y+1))){
                desalteraButao(x, y + 1);
            }
        }
        if (y-1 >= 0){
            if (dinamicaJogo.espacoDisponivel(new Casa(x,y-1))){
                desalteraButao(x, y - 1);
            }
        }
    }
    /**
     * Acoes.
     */

    /**
     * Servidor deve avaliar acao.
     */
    private void habilitarMovimentoPersonagem(){
        Personagem personagem = dinamicaJogo.casaOcupadaPorElemento(ultimaCasaPressionada);
         if (dinamicaJogo.moverParaCasaDesocupada(personagem,casaAcao)){
            atualizarCasaPorOcupante(ultimaCasaPressionada,Ocupante.Vazio);
            meuJogador.alterarCasaPersonagem(personagem,casaAcao);
            atualizarCasaPorOcupante(casaAcao,personagem.getNomeEspaco());
               }
    }

    private void criarPersonagemNaCasaAcao(Personagem personagem){
        if (atualizarCasaPorOcupante(casaAcao,personagem.getNomeEspaco()))
            meuJogador.addPersonagem(personagem);
    }

    private void criarOvelha(){
        criarPersonagemNaCasaAcao(new Ovelha(Ocupante.Ovelha,casaAcao));
    }
    private void criarCerca(){
        criarPersonagemNaCasaAcao(new Figurante(Ocupante.Cerca,casaAcao));
    }

}
