package com.flavioroberto.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

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


    public FlappyBird() {
    }


    @Override
    public void create() {
        batch = new SpriteBatch();
        bird = new Texture[3];
        bird[0] = new Texture("passaro1.png");
        bird[1] = new Texture("passaro2.png");
        bird[2] = new Texture("passaro3.png");
        background = new Texture("fundo.png");
        canoSuperior = new Texture("cano_topo.png");
        canoInferior = new Texture("cano_baixo.png");

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
        variacaoCenario++;
        velocidadeQueda++;
        //conta no tempo de fps do dispositivo
        variacao+=Gdx.graphics.getDeltaTime()*10;

        posicaoInicialCanoHorizontal -= Gdx.graphics.getDeltaTime()* 100;


        if(posicaoInicialCanoHorizontal + canoSuperior.getWidth() <= 0) {
            posicaoInicialCanoHorizontal = larguraDoDispositivo + canoSuperior.getWidth();
            alturaRandomica = numeroRandomico.nextInt(400) - 200;
        }
        if(variacao > 2)
            variacao = 0;

        if(Gdx.input.justTouched())
            velocidadeQueda=-20;

        if(posicaoInicialVertical > 0 || velocidadeQueda < 0)
            posicaoInicialVertical = posicaoInicialVertical-velocidadeQueda;


        //inicializa o processo de renderização por fps
        batch.begin();

        batch.draw(background,0, 0,larguraDoDispositivo,alturaDoDispositivo);

        //desenhando a imagem passaro
        batch.draw(bird[(int)variacao], 120, posicaoInicialVertical);

        //desenhando o cano
        batch.draw(canoSuperior,posicaoInicialCanoHorizontal,alturaDoDispositivo/2 + espacoEntreOsCanos + alturaRandomica);

        batch.draw(canoInferior,posicaoInicialCanoHorizontal,alturaDoDispositivo / 2 - canoInferior.getHeight() +alturaRandomica - espacoEntreOsCanos);
        //finaliza o processo de renderização
        batch.end();
    }

}
