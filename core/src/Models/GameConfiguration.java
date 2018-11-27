package Models;

public class GameConfiguration {

	private char configurationFlagMute;
	private int configurationVolume;
	private int configurationBrightness;
	
	
	public GameConfiguration(char configurationFlagMute , int configurationVolume , int configurationBrightness){
		
		this.configurationFlagMute = configurationFlagMute;
		this.configurationVolume = configurationVolume;
		this.configurationBrightness = configurationBrightness;
	}
	
	public GameConfiguration(){
		
		this.configurationFlagMute = 'N';
		this.configurationVolume = 6;
		this.configurationBrightness = 6;
	}
	
	public float getVolumeForGame() {
		
		float result = 0.4f;
		result += (float) this.configurationVolume / 10;;
		return  result;
	}
	
	public float getBrightnessForGame() {
		
		float result = 0.4f;
		result += (float) this.configurationBrightness / 10;;
		return  result;
	}
	
	public char getConfigurationFlagMute() {
		
		return this.configurationFlagMute;
	}
	
	public void setConfigurationFlagMute(char configurationFlagMute) {
		
		this.configurationFlagMute = configurationFlagMute;
	}
	
	public int getConfigurationVolume() {
		
		return this.configurationVolume;
	}
	
	public void setConfigurationVolume(int configurationVolume) {
		
		this.configurationVolume = configurationVolume;
	}
	
	public int getConfigurationBrightness() {
		
		return this.configurationBrightness;
	}
	
	public void setConfigurationBrightness(int configurationBrightness) {
		
		this.configurationBrightness = configurationBrightness;
	}
	
}
