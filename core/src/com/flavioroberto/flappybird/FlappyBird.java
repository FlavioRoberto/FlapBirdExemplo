package com.flavioroberto.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.awt.Color;
import java.util.Random;

public class FlappyBird extends ApplicationAdapter {

    //gera os sprites para as animações
    private SpriteBatch batch;
    private Texture[] bird;
    private Texture background;
    private Texture canoSuperior;
    private Texture canoInferior;

    //atributos configuracao
    private int larguraDoDispositivo, alturaDoDispositivo;
    private float variacao = 0;
    private float variacaoCenario = 0;
    private float velocidadeQueda = 0;
    private float posicaoInicialVertical;
    private float posicaoInicialCanoHorizontal, posicaoInicialCanoVertical;
    private float espacoEntreOsCanos;
    private Random numeroRandomico;
    private float alturaRandomica;
    private int estadoDoJogo;
    private int pontuacao = 0;
    private BitmapFont fontPontuacao;

    public FlappyBird() {
    }


    @Override
    public void create() {
        estadoDoJogo = 0;
        batch = new SpriteBatch();
        bird = new Texture[3];
        bird[0] = new Texture("passaro1.png");
        bird[1] = new Texture("passaro2.png");
        bird[2] = new Texture("passaro3.png");
        background = new Texture("fundo.png");
        canoSuperior = new Texture("cano_topo.png");
        canoInferior = new Texture("cano_baixo.png");

        fontPontuacao = new BitmapFont();
        fontPontuacao.setColor(com.badlogic.gdx.graphics.Color.WHITE);
        fontPontuacao.getData().setScale(8);


        numeroRandomico = new Random();
        alturaDoDispositivo = Gdx.graphics.getHeight();
        larguraDoDispositivo = Gdx.graphics.getWidth();
        espacoEntreOsCanos = bird[0].getHeight() * 3;
        posicaoInicialVertical = alturaDoDispositivo / 2 - bird[0].getHeight();
        posicaoInicialCanoHorizontal = larguraDoDispositivo - canoSuperior.getWidth();
        background.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.MirroredRepeat);
    }

    @Override
    public void render() {


        //animacao passaro
        if(estadoDoJogo != 2)
            variacao += Gdx.graphics.getDeltaTime() * 10;

        if (variacao > 2)
            variacao = 0;
        //fim config animacao

        if(estadoDoJogo == 0 || estadoDoJogo == 2)
        {
            if (Gdx.input.justTouched())
                estadoDoJogo = 1;
        }else {

            variacaoCenario++;
            velocidadeQueda++;
            //conta no tempo de fps do dispositivo

            posicaoInicialCanoHorizontal -= Gdx.graphics.getDeltaTime() * 100;


            if (posicaoInicialCanoHorizontal + canoSuperior.getWidth() <= 0) {
                posicaoInicialCanoHorizontal = larguraDoDispositivo + canoSuperior.getWidth();
                alturaRandomica = numeroRandomico.nextInt(400) - 200;
                pontuacao++;

            }

            if (Gdx.input.justTouched()) {
                velocidadeQueda = -20;
                estadoDoJogo = 1;
            }

            if (posicaoInicialVertical > 0 || velocidadeQueda < 0)
                posicaoInicialVertical = posicaoInicialVertical - velocidadeQueda;

            //condicao game over
            if(posicaoInicialVertical <=0 ) {
                pontuacao = 0;
                estadoDoJogo = 2;
                posicaoInicialVertical =  bird[0].getHeight()-30;
            }
        }

        //inicializa o processo de renderização por fps
        batch.begin();

        batch.draw(background,0, 0,larguraDoDispositivo,alturaDoDispositivo);

        //desenhando a imagem passaro
        batch.draw(bird[(int)variacao], 120, posicaoInicialVertical);

        //desenhando o cano
        batch.draw(canoSuperior,posicaoInicialCanoHorizontal,alturaDoDispositivo/2 + espacoEntreOsCanos + alturaRandomica);

        batch.draw(canoInferior,posicaoInicialCanoHorizontal,alturaDoDispositivo / 2 - canoInferior.getHeight() +alturaRandomica - espacoEntreOsCanos);

        fontPontuacao.draw(batch,String.valueOf(pontuacao),larguraDoDispositivo/2,alturaDoDispositivo-50);

        //finaliza o processo de renderização
        batch.end();


    }

}
