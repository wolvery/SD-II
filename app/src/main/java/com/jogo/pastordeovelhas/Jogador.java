package com.jogo.pastordeovelhas;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Classe responsavel para cada jogador adversario.
 */
public class Jogador {
    private ArrayList<Personagem> personagens;
    private Lobo meuLobo;
    private ArrayList<Ovelha> grupoDeOvelhas;

    public Jogador(ArrayList<Personagem> personagens){
        this.personagens = personagens; grupoDeOvelhas = new ArrayList<Ovelha>();
    }
    public ArrayList<Personagem> getPersonagens(){
        return personagens;
    }
    /**
     * Avalia se o espaco eh do jogador.
     * @param casaAVerificar
     * @return
     */
    public Boolean casaPertenceAoJogador(Casa casaAVerificar) {
        Iterator<Personagem> iter = personagens.iterator();
        if (meuLobo.getCasaPersonagem().comparaCasa(casaAVerificar)){
            return true;
        }
        while (iter.hasNext()) {
            Personagem elemento = iter.next();
            if (elemento.getCasaPersonagem().comparaCasa(casaAVerificar)) {
                return true;
            }
        }
        Iterator<Ovelha> iterOvelha = grupoDeOvelhas.iterator();
        while (iterOvelha.hasNext()) {
            Personagem elemento = iterOvelha.next();
            if (elemento.getCasaPersonagem().comparaCasa(casaAVerificar) ){
                return true;
            }
        }

        return false;
    }
    public void addPersonagem(Personagem personagem){
        if (personagem.getNomeEspaco().equals(Ocupante.Ovelha)){
            grupoDeOvelhas.add((Ovelha) personagem);
        }
        else if (personagem.getNomeEspaco().equals(Ocupante.Lobo)){
            meuLobo = (Lobo) personagem;
        }
        else {
            personagens.add(personagem);
        }
    }
    /**
     * Casa ocupada por Personagem
     * @param casaAVerificar
     * @return Personagem contido no Array.
     */
    public Personagem casaOcupadaPorElemento(Casa casaAVerificar) {
        Iterator<Personagem> iter = personagens.iterator();
        while (iter.hasNext()) {
            Personagem elemento = iter.next();
            if (elemento.getCasaPersonagem().comparaCasa(casaAVerificar)) {
                return elemento;
            }
        }
        return null;
    }
    /**
     * Casa ocupada por Personagem
     * @param casaAVerificar
     * @return Personagem contido no Array.
     */
    public Ovelha casaOcupadaPorOvelha(Casa casaAVerificar) {
        Iterator<Ovelha> iter = grupoDeOvelhas.iterator();
        while (iter.hasNext()) {
            Ovelha elemento = iter.next();
            if (elemento.getCasaPersonagem().comparaCasa(casaAVerificar)) {
                return elemento;
            }
        }
        return null;
    }
    public void removePersonagem(Personagem personagem){
        if (grupoDeOvelhas.contains((Ovelha) personagem))
            grupoDeOvelhas.remove((Ovelha) personagem);
        else if (personagens.contains(personagem))
            personagens.remove(personagem);
    }

    public void alterarCasaPersonagem(Personagem personagem, Casa casaNova){
        switch(personagem.getNomeEspaco()){
            case Lobo:
                meuLobo.setCasaPersonagem(casaNova);
                break;
            case Pastor:
                personagens.remove(personagem);
                personagem.setCasaPersonagem(casaNova);
                personagens.add(personagem);
                break;
            default:break;
        }
    }

    public ArrayList<Ovelha> getOvelhas(){
        return grupoDeOvelhas
                ;
    }
    public void setGrupoDeOvelhas(ArrayList<Ovelha> ovelhas){this.grupoDeOvelhas = ovelhas;}
    public void addOvelha(Ovelha ovelha){
        grupoDeOvelhas.add(ovelha);
    }
    public void removeOvelha(Ovelha ovelha){
        grupoDeOvelhas.remove(ovelha);
    }

    /**
     * Remove ovelha caso esteja morta.
     * @param ovelha
     * @return
     */
    public Boolean atualizaEstadoOvelha(Ovelha ovelha){
        grupoDeOvelhas.remove(ovelha);
        ovelha.vidaAtualizaFome();
        ovelha.vidaOvelhaAtaque();
        if (ovelha.ovelhaViva()) {
            grupoDeOvelhas.add(ovelha);
            return true;
        }
        return false;
    }
    /**
     * Alimenta ovelha.
     * @param ovelha
     * @return
     */
    public Boolean alimentaOvelha(Ovelha ovelha){
        grupoDeOvelhas.remove(ovelha);
        ovelha.alimentarOvelha();
        if (ovelha.ovelhaViva()) {
            grupoDeOvelhas.add(ovelha);
            return true;
        }
        return false;
    }

    public Lobo getMeuLobo(){
        return meuLobo;
    }
    public void setMeuLobo(Lobo novo){
        this.meuLobo = novo;
    }


}
