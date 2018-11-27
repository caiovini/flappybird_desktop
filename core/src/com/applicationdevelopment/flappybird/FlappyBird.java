package com.applicationdevelopment.flappybird;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import Database.GameDAO;
import Models.GameConfiguration;
import java.util.Random;


public class FlappyBird extends Game {

	
	private final boolean hasSound;
	private final float brightness;
	private final float volume;
	private String playerName;
	private ScoreScreen frame;
	
	private SpriteBatch batch;
	private Texture background;
	
    private Texture[] birds;
    private Sprite spriteBird;
    private double rotatioDegrees = 0.00;
    
    private Texture bottomPipe;
    private Texture topPipe;
    private Texture gameOver;
    
    private final String PRESS_ENTER_MESSAGE = "Press enter to start!";
    private final String PRESS_HOME_MESSAGE = "Press home to reinitialize!";
    private final String RESUME_GAME_MESSAGE = "RESUME GAME";
    private final String QUIT_MESSAGE = "QUIT";
    
    private final String DIE_SOUND_PATH = "sounds/die.wav";
    private final String HIT_SOUND_PATH = "sounds/hit.wav";
    private final String POINT_SOUND_PATH = "sounds/point.wav";
    private final String WING_SOUND_PATH = "sounds/wing.wav";
    
    
    //Sounds
    private Sound sound;
    
    /*
     * RandomNumber is used to create
     * pipes positions randomically
     */
    private Random randomNumber;
    
    /*
     *  This is used to check whenever 
     *  there is a collision between the bird
     *  and the pipes. 
     */
    private Circle circleBird;
    private Rectangle bottomPipeRectangle;
    private Rectangle topPipeRectangle;
    
    
    /*
     *  0=> Game not initialized 
     *  1=> Game initialized
     *  2=> Game is over
     *  3=> Game paused
     */
    private int stateOfTheGame = 0;
	
    private float variation = 0;
    private float lineBlink = 0;
    private float fallSpeed = 0;
    private float verticalInitialPosition;
    private float movementHorizontalPipe;
    private float spaceBetweenPipes;
    private float deltaTime;
    private float seconds;
    private float gameTime;
    private float timer;
    private float heightBetweenPipes;
    private boolean scored = false;
    private int pipeSpeed = 200;
	
    /*
     * screenWidth => Define the width of the screen
     * screenHeight => Define the height of the screen
     */
	private float screenWidth;
    private float screenHeight;
    
    private float lineHeight;
    private float lineWidth;
    private String outlineString;
    
    //Handle score
    private int score;
    private String scoreMessage = "Score: 0";    
    private BitmapFont scoreFont;
    private BitmapFont stopWatchFont;
    private BitmapFont message;
    private BitmapFont outline;
    
    
    //Camera view and configuration
    private OrthographicCamera camera;
    private Viewport viewport;
    private final float VIRTUAL_WIDTH = 768;
    private final float VIRTUAL_HEIGHT = 1024;
    
    
	public FlappyBird(GameConfiguration gameConfiguration , String playerName ){
    	
    	this.hasSound = gameConfiguration.getConfigurationFlagMute() == 'N';
    	this.brightness = gameConfiguration.getBrightnessForGame();
    	this.volume = gameConfiguration.getVolumeForGame();
    	this.playerName = playerName;
    }
	
	@Override
	public void create () {
		
		batch = new SpriteBatch();
		batch.setColor(1f, 1f, 1f, this.brightness);
		
		randomNumber = new Random();
		circleBird = new Circle();
		
		scoreFont = new BitmapFont();
		scoreFont.setColor(Color.YELLOW);
		scoreFont.getData().setScale(2);
		
		stopWatchFont = new BitmapFont();
		stopWatchFont.setColor(Color.YELLOW);
		stopWatchFont.getData().setScale(2);
		
		message = new BitmapFont();
		message.setColor(Color.YELLOW);
		message.getData().setScale(2);
		
		outline = new BitmapFont();
		outline.setColor(Color.YELLOW);
		outline.getData().setScale(3);
		
		
		birds = new Texture[3];
		birds[0] = new Texture("images/game_screen/bird1.png");
		birds[1] = new Texture("images/game_screen/bird2.png");
		birds[2] = new Texture("images/game_screen/bird3.png");
		
		background = new Texture("images/game_screen/background.png");
		bottomPipe = new Texture("images/game_screen/bottom_pipe.png");
		topPipe = new Texture("images/game_screen/top_pipe.png");
		gameOver = new Texture("images/game_screen/game_over.png");
		
		/*-----------------------
		 | Camera configuration |
		 ----------------------*/
		
		camera = new OrthographicCamera();
		camera.position.set(VIRTUAL_WIDTH / 2 , VIRTUAL_HEIGHT / 2 , 0);
		viewport = new StretchViewport(VIRTUAL_WIDTH , VIRTUAL_HEIGHT , camera);
		
		screenWidth = VIRTUAL_WIDTH;
		screenHeight  = VIRTUAL_HEIGHT;
		
		verticalInitialPosition = screenHeight / 2;
		movementHorizontalPipe = screenWidth;
		
		
		lineWidth = screenWidth / 2 - 80;
		lineHeight = screenHeight / 2;
		outlineString = "________";
		
		//Defined initial space of 300 
		spaceBetweenPipes = 300;
		
	}

	@Override
	public void render () {
		
		
		//If user presses esc, pause the game
		if( Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) && this.stateOfTheGame != 2)
			this.stateOfTheGame = 3;  
		
		camera.update();
		
		//Clear all previous frames
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		gameTime = Gdx.graphics.getDeltaTime();
		deltaTime = Gdx.graphics.getDeltaTime();
		variation += deltaTime * 10;
		lineBlink += deltaTime * 10;
		
		if(variation > 2) variation = 0;
		
		switch (stateOfTheGame){
		
			case 0: 
			
				/*
			 	* Not initialized yet
			 	* If user presses enter 
			 	* game starts
			 	*/
				timer = 0;
				seconds = 0;
				if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) stateOfTheGame = 1;
			
				break;
			case 1:
				
				/*
				 * Initialized
				 * Start moving the bird down by increasing the fallSpeed
				 */
				timer += gameTime;
		        seconds = (timer / 60.0f) * 60.0f;
				
				
				fallSpeed ++;
				
				//Set rotation down of the bird when it is falling
				if(fallSpeed > 0) rotatioDegrees = -45.00;
				
				if(verticalInitialPosition > 0 || fallSpeed < 0) verticalInitialPosition -= fallSpeed;
			
				movementHorizontalPipe -= deltaTime * pipeSpeed;
				
				/*
				 * Check if user pressed key up on keyboard
				 * in this case the bird should move up
				 */
				if(Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
					
					rotatioDegrees = 45.00;
					fallSpeed = -15;
					sound = Gdx.audio.newSound(Gdx.files.internal(WING_SOUND_PATH));
					if(hasSound) sound.play(this.volume);
				}
				
				//Check if pipe is out of the screen borders
				if(movementHorizontalPipe < - topPipe.getWidth()) {
					
					movementHorizontalPipe = screenWidth;
					heightBetweenPipes = randomNumber.nextInt(400) - 200;
					scored = false;
				}
				
				//Check score
				if(movementHorizontalPipe < 130) {
					if( !scored ) {
						
						score ++;
						sound = Gdx.audio.newSound(Gdx.files.internal(POINT_SOUND_PATH));
						if(hasSound) sound.play(this.volume);
						
						/*
						 * Increase speed of the 
						 * pipes shown by 50  
						 * each 5 points
						 */
						if(score % 5 == 0) {
							
							pipeSpeed += 50;
						}
						
						scoreMessage = "Score : " + score;
						scored = true;
					}
					
				}
				
				break;
			case 2: 	
				
				//Game over
				
				fallSpeed ++;
				if(verticalInitialPosition > 0 || fallSpeed < 0) verticalInitialPosition -= fallSpeed;
				
							
				//If user presses key home the game will be restarted
				if( Gdx.input.isKeyPressed(Input.Keys.HOME)){
					
                    stateOfTheGame = 0;
                    fallSpeed = 0;
                    score = 0;
                    rotatioDegrees = 0.00;
                    pipeSpeed = 200;
                    scoreMessage = "Score : " + score;
                    movementHorizontalPipe = screenWidth;
                    verticalInitialPosition = screenHeight / 2;
                }
				
				break;
				
			case 3:
				
				/*
			 	* Game paused
			 	* If user presses enter check either if resume game
			 	* or quit is selected
			 	*/
				
				if(frame != null) {
					if(frame.isEndGame()) {
						
						Gdx.app.exit();
					}
				}
				
				
				if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
					
					if(lineHeight == screenHeight / 2) stateOfTheGame = 1;  //Resume game
					if(lineHeight == screenHeight / 2 - 75) this.dispose(); //Quit game
				}
				
				if(Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
					
					lineWidth = screenWidth / 2 - 80;
					lineHeight = screenHeight / 2;
					outlineString = "________";
				}
				
				if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
					
					lineWidth = screenWidth / 2 - 13;
					lineHeight = screenHeight / 2 - 75;
					outlineString = "__";
				}
				
				break;
				
			}
		
		
		//Set camera projection data
		batch.setProjectionMatrix( camera.combined );
		
		//Beginning of the process
		batch.begin();
		
		batch.draw(background, 0, 0, screenWidth, screenHeight);
		batch.draw(topPipe, movementHorizontalPipe , screenHeight / 2 + spaceBetweenPipes / 2 + heightBetweenPipes );
		batch.draw(bottomPipe, movementHorizontalPipe, screenHeight / 2 - bottomPipe.getHeight() - spaceBetweenPipes / 2 + heightBetweenPipes);
		
	
			
		spriteBird = new Sprite(birds[(int) variation]);
		spriteBird.rotate((float) rotatioDegrees);
		spriteBird.setPosition(120 , verticalInitialPosition);
		spriteBird.draw(batch);

		
		scoreFont.draw(batch, scoreMessage, screenWidth / 2 - 60, screenHeight - 50);
		stopWatchFont.draw(batch, String.format("%.3fs",  seconds), screenWidth / 2 -340, screenHeight - 50);
		
		switch (stateOfTheGame) {
		
			case 0:
				
				
				message.draw(batch, PRESS_ENTER_MESSAGE, screenWidth / 2 - 120, screenHeight / 2 + 30 );
				
				break;
		
			case 2:
				
				message.draw(batch, PRESS_HOME_MESSAGE, screenWidth / 2 - 160, screenHeight / 2 - gameOver.getHeight());		
				batch.draw(gameOver, screenWidth / 2 - gameOver.getWidth() / 2, screenHeight / 2);
				
				break;
				
				
			case 3:
				
				message.draw(batch, RESUME_GAME_MESSAGE , screenWidth - 480 , screenHeight / 2 );
				message.draw(batch, QUIT_MESSAGE , screenWidth / 2 - 20 , screenHeight / 2 - 70 );
				
				//Resume game
				if(lineBlink > 5) {
					if(lineBlink > 10) lineBlink = 0;
					outline.draw(batch, outlineString , lineWidth, lineHeight);
				}
				
				break;
				
		
		}
		
		
		batch.end();
		
		//Draw circle for the birds (collision test)
		circleBird.set(120 + birds[0].getWidth() / 2, verticalInitialPosition + birds[0].getHeight() / 2, birds[0].getWidth() / 2);
		
		
		//Draw rectangle for the bottom pipe (collision test)
		bottomPipeRectangle = new Rectangle(
				movementHorizontalPipe, screenHeight / 2 - bottomPipe.getHeight() - spaceBetweenPipes / 2 + heightBetweenPipes,
				bottomPipe.getWidth() , bottomPipe.getHeight());
		
		
		
		//Draw rectangle for the top pipe (collision test)
		topPipeRectangle = new Rectangle(
				movementHorizontalPipe, screenHeight / 2 + spaceBetweenPipes / 2 + heightBetweenPipes,
				topPipe.getWidth() , topPipe.getHeight());
		
		//Collision test
        if(( Intersector.overlaps( circleBird, bottomPipeRectangle ) || Intersector.overlaps(circleBird, topPipeRectangle))
        	 && (stateOfTheGame != 2)) {
        	
        	//Wow, there has been a collision
        	sound = Gdx.audio.newSound(Gdx.files.internal(HIT_SOUND_PATH));
        	if(hasSound) sound.play(this.volume);
			stateOfTheGame = 2;
			scored = false;
			
			if (score > 0) {
				
				GameDAO database = new GameDAO();
				database.insertNewScoreTime(this.score, this.playerName , this.seconds);
			}
			
        //Also check if the bird is out of the screen borders	   
        }else if(( verticalInitialPosition <= 0 || verticalInitialPosition >= screenHeight )
         		   && (stateOfTheGame != 2)) {
        
        	//Wow, the bird is out of the borders of the screen
        	sound = Gdx.audio.newSound(Gdx.files.internal(DIE_SOUND_PATH));
        	if(hasSound) sound.play(this.volume);
            stateOfTheGame = 2;
            scored = false;
            
			if (score > 0) {
				
				GameDAO database = new GameDAO();
				database.insertNewScoreTime(this.score, this.playerName , this.seconds);
			}
            
        }else {
        	
        	//Do nothing
        }
	}
	
    @Override
    public void resize(int width, int height) {
    	
        viewport.update(width, height);
    }
    
    
    @Override
    public void dispose() {

        // TODO Auto-generated method stub
    	
    	frame = new ScoreScreen(this.playerName);
    	frame.focus();
    }
}
