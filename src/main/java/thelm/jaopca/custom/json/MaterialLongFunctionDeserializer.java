package thelm.jaopca.custom.json;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.ToLongFunction;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import thelm.jaopca.api.helpers.IJsonHelper;
import thelm.jaopca.api.materials.IMaterial;
import thelm.jaopca.custom.CustomModule;
import thelm.jaopca.materials.MaterialHandler;
import thelm.jaopca.utils.JsonHelper;

public class MaterialLongFunctionDeserializer implements JsonDeserializer<ToLongFunction<IMaterial>> {

	public static final MaterialLongFunctionDeserializer INSTANCE = new MaterialLongFunctionDeserializer();

	private MaterialLongFunctionDeserializer() {}

	@Override
	public ToLongFunction<IMaterial> deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		IJsonHelper helper = JsonHelper.INSTANCE;
		JsonObject json = helper.getJsonObject(jsonElement, "object");
		long defaultValue = helper.getLong(json, "default");
		Map<IMaterial, Long> map = new TreeMap<>();
		if(json.has("materialTypes")) {
			JsonObject materialTypesJson = helper.getJsonObject(json, "materialTypes");
			for(Map.Entry<String, JsonElement> entry : materialTypesJson.entrySet()) {
				long materialTypeValue = helper.getLong(entry.getValue(), "element");
				switch(entry.getKey()) {
				case "ingot":
					MaterialHandler.getMaterials().stream().
					filter(m->m.getType().isIngot()).
					forEach(m->map.put(m, materialTypeValue));
					break;
				case "gem":
					MaterialHandler.getMaterials().stream().
					filter(m->m.getType().isGem()).
					forEach(m->map.put(m, materialTypeValue));
					break;
				case "crystal":
					MaterialHandler.getMaterials().stream().
					filter(m->m.getType().isCrystal()).
					forEach(m->map.put(m, materialTypeValue));
					break;
				case "dust":
					MaterialHandler.getMaterials().stream().
					filter(m->m.getType().isDust()).
					forEach(m->map.put(m, materialTypeValue));
					break;
				}
			}
		}
		if(json.has("materials")) {
			JsonObject materialsJson = json.get("materials").getAsJsonObject();
			for(Map.Entry<String, JsonElement> entry : materialsJson.entrySet()) {
				if(MaterialHandler.containsMaterial(entry.getKey())) {
					map.put(MaterialHandler.getMaterial(entry.getKey()), helper.getLong(entry.getValue(), "element"));
				}
			}
		}
		if(json.has("config")) {
			if(helper.getBoolean(json, "config")) {
				String path = helper.getString(json, "path");
				String comment;
				if(json.has("comment")) {
					comment = helper.getString(json, "comment");
				}
				else {
					comment = "";
				}
				CustomModule.instance.addCustomConfigDefiner((material, config)->{
					map.put(material, config.getDefinedLong(path, map.getOrDefault(material, defaultValue), comment));
				});
			}
		}
		return material->map.getOrDefault(material, defaultValue);
	}
}
