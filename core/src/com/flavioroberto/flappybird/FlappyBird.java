package com.flavioroberto.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FlappyBird extends ApplicationAdapter {

    //gera os sprites para as animações
    private SpriteBatch batch;
    private Texture[] bird;
    private Texture background;

    //atributos configuracao
    private int larguraDoDispositivo, alturaDoDispositivo;
    private float variacao = 0;
    private float velocidadeQueda = 0;
    private float posicaoInicialVertical;


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
        alturaDoDispositivo = Gdx.graphics.getHeight();
        larguraDoDispositivo = Gdx.graphics.getWidth();
        posicaoInicialVertical = alturaDoDispositivo / 2 - bird[0].getHeight();
    }

    @Override
    public void render() {
        velocidadeQueda++;
        //conta no tempo de fps do dispositivo
        variacao+=Gdx.graphics.getDeltaTime()*10;

        if(variacao > 2)
            variacao = 0;

        //inicializa o processo de renderização por fps
        batch.begin();

        batch.draw(background, 0, 0, larguraDoDispositivo,alturaDoDispositivo);

        if(posicaoInicialVertical > 1)
           posicaoInicialVertical = posicaoInicialVertical-velocidadeQueda;

        //desenhando a imagem
        batch.draw(bird[(int)variacao], 30, posicaoInicialVertical);


        //finaliza o processo de renderização
        batch.end();
    }

}
