package com.app.augmentedbizz.application.data;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.app.augmentedbizz.logging.DebugLog;
import com.app.augmentedbizz.services.entity.ServiceTransferEntity;
import com.app.augmentedbizz.services.entity.transfer.IndicatorServiceEntity;
import com.app.augmentedbizz.services.entity.transfer.IndicatorServiceEntity.TargetIndicator;
import com.app.augmentedbizz.services.entity.transfer.ModelServiceEntity;
import com.app.augmentedbizz.services.entity.transfer.TargetServiceEntity;
import com.app.augmentedbizz.services.service.BaseHttpService;
import com.app.augmentedbizz.services.service.repository.ModelHttpService;
import com.app.augmentedbizz.ui.renderer.OpenGLModel;
import com.app.augmentedbizz.ui.renderer.OpenGLModelConfiguration;
import com.app.augmentedbizz.ui.renderer.Texture;
import com.app.augmentedbizz.util.Base64;
import com.app.augmentedbizz.util.TypeConversion;

public class EntityConverter {
	
	public OpenGLModelConfiguration toOpenGLModelFrom(ServiceTransferEntity serviceTransferEntity, BaseHttpService calledService, int modelVersion) {
		ModelServiceEntity modelTransferEntity = (ModelServiceEntity)serviceTransferEntity;
		ModelHttpService modelHttpService = (ModelHttpService)calledService;
		
		String base64EncodedTexture = modelTransferEntity.getBase64EncodedTexture();
		InputStream inputStream = new ByteArrayInputStream(new byte[] {});
		
		try {
			inputStream = new ByteArrayInputStream(Base64.decode(base64EncodedTexture));
		} catch (IOException e) {
			DebugLog.loge("Unable to decode Base64-Image", e);
		}
		
		Texture texture = TypeConversion.toTextureFrom(inputStream);
		OpenGLModelConfiguration model = new OpenGLModelConfiguration(new OpenGLModel(
				modelHttpService.getModelId(),
				modelVersion,
				modelTransferEntity.getVertices(),
				modelTransferEntity.getNormals(),
				modelTransferEntity.getTexCoords(),
				modelTransferEntity.getIndices(),
				texture), 1);
		
		return model;
	}
	
	public Target toTargetFrom(ServiceTransferEntity serviceTransferEntity) {
		TargetServiceEntity targetServiceEntity = (TargetServiceEntity)serviceTransferEntity;
		Target target = new Target(targetServiceEntity.getTargetName(),
				targetServiceEntity.getModelId().intValue(),
				targetServiceEntity.getLatestModelVersion());
		
		return target;
	}
	
	public List<TargetIndicator> toIndicatorListFrom(ServiceTransferEntity serviceTransferEntity) {
		IndicatorServiceEntity indicatorServiceEntity = (IndicatorServiceEntity)serviceTransferEntity;
		return indicatorServiceEntity.getIndicatorList();
	}

}
