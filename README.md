# Yeelight Java API

Remote control your Xiaomi Yeelight through WiFi with this Java API.

## Getting Started

Checkout this project and import as a dependency to your project.

Artifactory (maven/gradle) currently not supported.

## Prepare your Yeelight

1. Initialize your Yeelight with the official App
2. Turn on developer mode on the Yeelight

## Examples

### Discovery

Use YeelightDiscovery class to find all devices on the same WiFi network.

```
new YeelightDiscovery().start(new DiscoveryListener() {
			
	public void onError(Exception e) {
	}
	
	public void onDiscoveryFinished(Collection<YeelightDevice> devices) {
	}
	
	public void onDeviceFound(YeelightDevice device) 
	{
		System.out.println(device.getName());
	}
});
```

### Control

Use YeelightController class to remote control your device.

```
YeelightController controller=new YeelightController(device);
				
controller.setPower(true, Effect.SMOOTH, 500, new OnCommandListener() {
	
	public void onCommandFinished(YeelightDevice device, Features feature, Object[] response) {
		System.out.println("Command Finished! Yeelight turned on.");
	}
	
	public void onCommandError(YeelightDevice device, Features feature, YeelightError error) {
		System.out.println("Command Error! "+error.getMessage());
	}
});
```

### Documentation

Documentation currently in progress

Use official Yeelight API documentation as an example.
Link: http://www.yeelight.com/download/Yeelight_Inter-Operation_Spec.pdf

## Author

* **Szabó Tamás** - [Szabot0412](https://github.com/szabot0412)
