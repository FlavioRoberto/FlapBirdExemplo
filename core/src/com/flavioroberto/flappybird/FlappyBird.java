package com.flavioroberto.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.Random;

public class FlappyBird extends ApplicationAdapter {

    //gera os sprites para as animações
    private SpriteBatch batch;
    private Texture[] bird;
    private Texture background;
    private Texture canoSuperior;
    private Texture canoInferior;
    private Texture gameOver;
    private BitmapFont mensagem;

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
    private Circle passarocirculo;
    private Rectangle canoTopoRetangulo, canoBaixoRetangulo;
    private ShapeRenderer shapeRenderer;

    public FlappyBird() {
    }


    @Override
    public void create() {
        estadoDoJogo = 0;
        batch = new SpriteBatch();
        gameOver = new Texture("game_over.png");
        bird = new Texture[3];
        bird[0] = new Texture("passaro1.png");
        bird[1] = new Texture("passaro2.png");
        bird[2] = new Texture("passaro3.png");
        background = new Texture("fundo.png");
        canoSuperior = new Texture("cano_topo.png");
        canoInferior = new Texture("cano_baixo.png");

        shapeRenderer = new ShapeRenderer();

        //configuracao bitmap font
        fontPontuacao = new BitmapFont();
        fontPontuacao.setColor(com.badlogic.gdx.graphics.Color.WHITE);
        fontPontuacao.getData().setScale(8);

        //configuracao bitmap imagem
        mensagem = new BitmapFont();
        mensagem.setColor(com.badlogic.gdx.graphics.Color.WHITE);
        mensagem.getData().setScale(3);

        passarocirculo = new Circle();
        canoBaixoRetangulo = new Rectangle();
        canoTopoRetangulo = new Rectangle();
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

        velocidadeQueda++;

        //conta no tempo de fps do dispositivo
        posicaoInicialCanoHorizontal -= Gdx.graphics.getDeltaTime() * 100;

        //verifica se passaro cai no chao
        if ((posicaoInicialVertical > 0 || velocidadeQueda < 0) && estadoDoJogo != 0)
            posicaoInicialVertical = posicaoInicialVertical - velocidadeQueda;

        //animacao passaro
        if (estadoDoJogo != 2)
            variacao += Gdx.graphics.getDeltaTime() * 10;

        if (variacao > 2)
            variacao = 0;
        //fim config animacao

        //Jogo parado
        if (estadoDoJogo == 0) {
            if (Gdx.input.justTouched())
                estadoDoJogo = 1;

        }

        if (estadoDoJogo == 1) {

            //JOGO INICIADO
            if (estadoDoJogo == 1) {

                //toque para voar
                if (Gdx.input.justTouched()) {
                    velocidadeQueda = -20;
                }

                //movimento cano
                if (posicaoInicialCanoHorizontal + canoSuperior.getWidth() <= 0) {
                    posicaoInicialCanoHorizontal = larguraDoDispositivo + canoSuperior.getWidth();
                    alturaRandomica = numeroRandomico.nextInt(400) - 200;
                    pontuacao++;
                }

                //condicao game over
                if (posicaoInicialVertical <= 0 || posicaoInicialVertical >= alturaDoDispositivo) {
                    estadoDoJogo = 2;
                }

            }
        }

        //perdeu
        if (estadoDoJogo == 2) {
            if (Gdx.input.isTouched()) {
                estadoDoJogo = 0;
                pontuacao = 0;
                velocidadeQueda = 0;
                posicaoInicialVertical = alturaDoDispositivo / 2;
                posicaoInicialCanoHorizontal = larguraDoDispositivo;
            }
        }

        //verificadno colisoes
        if (Intersector.overlaps(passarocirculo, canoTopoRetangulo) || Intersector.overlaps(passarocirculo, canoBaixoRetangulo)) {
            estadoDoJogo = 2;
            Gdx.app.log("Colisao", "Colidiu");
        }


        //inicializa o processo de renderização por fps
        batch.begin();

        batch.draw(background, 0, 0, larguraDoDispositivo, alturaDoDispositivo);

        //desenhando a imagem passaro
        batch.draw(bird[(int) variacao], 120, posicaoInicialVertical);

        //desenhando o cano
        float posicaoCanoTopoVertical = alturaDoDispositivo / 2 + espacoEntreOsCanos + alturaRandomica;
        batch.draw(canoSuperior, posicaoInicialCanoHorizontal, posicaoCanoTopoVertical);

        float posicaoCanoBaixoVertical = alturaDoDispositivo / 2 - canoInferior.getHeight() + alturaRandomica - espacoEntreOsCanos;
        batch.draw(canoInferior, posicaoInicialCanoHorizontal, posicaoCanoBaixoVertical);


        //desenho pontuacao
        fontPontuacao.draw(batch, String.valueOf(pontuacao), larguraDoDispositivo / 2, alturaDoDispositivo - 50);

        //imagem gameOver
        if (estadoDoJogo == 2) {
            batch.draw(gameOver, larguraDoDispositivo / 2 - gameOver.getWidth() / 2, alturaDoDispositivo / 2);
            mensagem.draw(batch, "Toque para reiniciar", larguraDoDispositivo / 2 - gameOver.getWidth() / 2, alturaDoDispositivo / 2 - 20);
        }

        //finaliza o processo de renderização
        batch.end();


        //definindo objetos para o colisor
        passarocirculo.set(120 + bird[0].getWidth() / 2, posicaoInicialVertical + bird[0].getHeight() / 2, bird[0].getWidth() / 2);

        canoBaixoRetangulo = new Rectangle(posicaoInicialCanoHorizontal, posicaoCanoBaixoVertical,
                canoInferior.getWidth(), canoInferior.getHeight());

        canoTopoRetangulo = new Rectangle(posicaoInicialCanoHorizontal, posicaoCanoTopoVertical,
                canoSuperior.getWidth(), canoSuperior.getHeight());

    }

}
