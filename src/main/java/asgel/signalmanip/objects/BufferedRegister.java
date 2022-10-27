package asgel.signalmanip.objects;

import com.google.gson.JsonObject;

import asgel.core.gfx.Direction;
import asgel.core.gfx.Point;
import asgel.core.model.IParametersRequester;
import asgel.core.model.ModelOBJ;
import asgel.core.model.Pin;

public class BufferedRegister extends ModelOBJ {

	private boolean[] data;

	protected BufferedRegister(int x, int y, int size) {
		super("Buffered Register " + size + " bits", "BR" + size + "b", x, y, 48, 64, 5);
		pins[0] = new Pin(this, Direction.WEST, size, "DATA_IN", true).setIsSensible(false);
		pins[1] = new Pin(this, Direction.WEST, 1, "LOAD", true).setIsSensible(false);
		pins[2] = new Pin(this, Direction.WEST, 1, "ENABLE", true);
		pins[3] = new Pin(this, Direction.SOUTH, 1, "CLK", true);
		pins[4] = new Pin(this, Direction.EAST, size, "DATA_OUT", false);

		data = new boolean[size];
	}

	@Override
	public void update() {
		if (pins[1].getData()[0] && pins[3].getData()[0]) {
			data = ModelOBJ.cloneArray(pins[0].getData());
		}
		if (pins[2].getData()[0]) {
			pins[4].setData(data);
		} else {
			pins[4].clearData();
		}
	}

	@Override
	protected void toJsonInternal(JsonObject json) {
		json.addProperty("size", pins[0].getSize());
		json.add("data", ModelOBJ.toJsonArray(data));
	}

	public static BufferedRegister askFor(Point p, IParametersRequester req) {
		int[] params = req.getParametersAsInt("Size");
		if (params == null)
			return null;
		return new BufferedRegister((int) p.x, (int) p.y, params[0]);
	}

	private BufferedRegister setData(boolean[] data) {
		this.data = data;
		return this;
	}

	public static BufferedRegister fromJson(JsonObject json) {
		return new BufferedRegister(json.get("x").getAsInt(), json.get("y").getAsInt(), json.get("size").getAsInt())
				.setData(ModelOBJ.toBooleanArray(json.get("data").getAsJsonArray()));
	}
}