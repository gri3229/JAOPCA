package thelm.jaopca.localization;

import java.util.Arrays;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.translation.LanguageMap;
import thelm.jaopca.api.localization.ILocalizer;
import thelm.jaopca.api.materials.IMaterial;
import thelm.jaopca.utils.ApiImpl;

public class LocalizerDefault implements ILocalizer {

	private LocalizerDefault() {}

	public static final LocalizerDefault INSTANCE = new LocalizerDefault();

	@Override
	public ITextComponent localizeMaterialForm(String formTranslationKey, IMaterial material, String overrideKey) {
		LanguageMap languageMap = LanguageMap.getInstance();
		Map<String, String> locMap = ApiImpl.INSTANCE.currentMaterialLocalizationMap();
		if(languageMap.exists(overrideKey)) {
			return new TranslationTextComponent(overrideKey);
		}
		else if(locMap.containsKey(overrideKey)) {
			return new StringTextComponent(locMap.get(overrideKey));
		}
		String materialName;
		String materialKey = "jaopca.material."+material.getName();
		if(languageMap.exists(materialKey)) {
			materialName = languageMap.translateKey(materialKey);
		}
		else if(locMap.containsKey(materialKey)) {
			materialName = locMap.get(materialKey);
		}
		else {
			materialName = splitAndCapitalize(material.getName());
		}
		if(languageMap.exists(formTranslationKey) || !locMap.containsKey(formTranslationKey)) {
			return new TranslationTextComponent(formTranslationKey, materialName);
		}
		else {
			return new StringTextComponent(String.format(locMap.get(overrideKey), materialName));
		}
	}

	public static String splitAndCapitalize(String underscore) {
		return Arrays.stream(StringUtils.split(underscore, '_')).map(StringUtils::capitalize).reduce((s1, s2)->s1+' '+s2).orElse("");
	}
}
