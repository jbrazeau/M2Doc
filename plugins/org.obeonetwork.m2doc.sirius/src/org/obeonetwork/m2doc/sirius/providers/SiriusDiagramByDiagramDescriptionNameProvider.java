/*******************************************************************************
 * Copyright (c) 2016 Obeo. 
 *    All rights reserved. This program and the accompanying materials
 *    are made available under the terms of the Eclipse Public License v1.0
 *    which accompanies this distribution, and is available at
 *    http://www.eclipse.org/legal/epl-v10.html
 *     
 *     Contributors:
 *         Obeo - initial API and implementation
 *******************************************************************************/
package org.obeonetwork.m2doc.sirius.providers;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.internal.session.SessionTransientAttachment;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.description.DiagramDescription;
import org.eclipse.sirius.diagram.description.Layer;
import org.eclipse.sirius.ext.base.Option;
import org.eclipse.sirius.viewpoint.DRepresentationDescriptor;
import org.obeonetwork.m2doc.genconf.Generation;
import org.obeonetwork.m2doc.provider.IProvider;
import org.obeonetwork.m2doc.provider.OptionType;
import org.obeonetwork.m2doc.provider.ProviderConstants;
import org.obeonetwork.m2doc.provider.ProviderException;
import org.obeonetwork.m2doc.provider.ProviderValidationMessage;
import org.obeonetwork.m2doc.sirius.services.SiriusRepresentationUtils;
import org.obeonetwork.m2doc.sirius.util.OptionUtil;

/**
 * {@link SiriusDiagramByDiagramDescriptionNameProvider} are used to get Sirius diagrams images from all representations using a given root
 * eObject and a diagram description name.
 * 
 * @author pguilet<pierre.guilet@obeo.fr>
 */
@SuppressWarnings("restriction")
public class SiriusDiagramByDiagramDescriptionNameProvider extends AbstractSiriusDiagramImagesProvider {
    /**
     * The option key used to retrieve the {@link EObject} root of some diagram representations.
     */
    private static final String TARGET_ROOT_OBJECT_KEY = "object";
    /**
     * The key used in the map passed to {@link IProvider} to define the Sirius
     * representation title from which image will be computed.
     */
    private static final String DIAGRAM_DESCRIPTION_ID_KEY = "descriptionId";
    /**
     * the key used in the map passed to {@link IProvider} to define the value of the create option.
     */
    private static final String CREATE_ID_KEY = "create";
    /**
     * Value used to assert absent representation must be created.
     */
    private static final Object CREATE_VALUE = "true";

    /**
     * Default constructor.
     */
    public SiriusDiagramByDiagramDescriptionNameProvider() {
    }

    @Override
    public List<String> getRepresentationImagePath(ResourceSet resourceSetForModel, Map<String, Object> parameters)
            throws ProviderException {
        Generation generation = (Generation) parameters.get(ProviderConstants.CONF_ROOT_OBJECT_KEY);

        String rootPath = createTempDirectoryPath();
        Object diagramDescriptionName = parameters.get(DIAGRAM_DESCRIPTION_ID_KEY);
        Object targetRootObject = parameters.get(TARGET_ROOT_OBJECT_KEY);
        boolean createIfAbsent = CREATE_VALUE.equals(parameters.get(CREATE_ID_KEY));
        @SuppressWarnings("unchecked")
        List<String> diagramActivatedLayers = (List<String>) parameters
                .get(ProviderConstants.DIAGRAM_ACTIVATED_LAYERS_KEY);
        refreshRepresentations = OptionUtil.mustRefreshRepresentation(parameters);
        if (!(diagramDescriptionName instanceof String)) {
            throw new ProviderException(
                    "Image cannot be computed because no diagram description name has been provided to the provider \""
                        + this.getClass().getName() + "\"");
        } else if (!(targetRootObject instanceof EObject)) {
            throw new ProviderException(
                    "Image cannot be computed because no root EObject has been provided to the provider \""
                        + this.getClass().getName() + "\"");
        } else {
            EObject targetRootEObject = (EObject) targetRootObject;
            Option<SessionTransientAttachment> attachement = SessionTransientAttachment
                    .getSessionTransientAttachement(resourceSetForModel);
            if (!attachement.some()) {
                throw new ProviderException("Cannot find session associated to the models.");
            }
            Session session = attachement.get().getSession();
            checkDiagramDescriptionExist(session, (String) diagramDescriptionName);
            List<DRepresentationDescriptor> representations = SiriusRepresentationUtils
                    .getAssociatedRepresentationByDescriptionAndName(generation, targetRootEObject,
                            (String) diagramDescriptionName, session, createIfAbsent);

            final List<String> result;
            if (!representations.isEmpty() && representations.get(0).getDescription() instanceof DiagramDescription) {
                DDiagram resolvedDiagram = (DDiagram) representations.get(0).getRepresentation();
                result = SiriusRepresentationUtils.generateAndReturnDiagramImages(rootPath, session, createIfAbsent,
                        representations, getLayers(resolvedDiagram, diagramActivatedLayers));
            } else {
                result = SiriusRepresentationUtils.generateAndReturnDiagramImages(rootPath, session, createIfAbsent,
                        representations, Lists.<Layer> newArrayList());
            }

            return result;
        }
    }

    /**
     * Checks that the given description exists in the session. If not we throw an exception informing of the problem.
     * 
     * @param session
     *            the session were to find diagram description.
     * @param diagramDescriptionName
     *            the diagram description to find in the session.
     * @throws ProviderException
     *             if the diagram description does not exist in the session.
     */
    private static void checkDiagramDescriptionExist(Session session, String diagramDescriptionName)
            throws ProviderException {
        if (SiriusRepresentationUtils.findDescription(session, diagramDescriptionName) == null) {
            throw new ProviderException("The provided diagram description '" + diagramDescriptionName
                + "' does not exist in the loaded aird");
        }

    }

    @Override
    public Map<String, OptionType> getOptionTypes() {
        Map<String, OptionType> optionsMap = new HashMap<>();
        optionsMap.put(TARGET_ROOT_OBJECT_KEY, OptionType.AQL_EXPRESSION);
        optionsMap.put(DIAGRAM_DESCRIPTION_ID_KEY, OptionType.STRING);
        return optionsMap;
    }

    @Override
    public boolean isDefault() {
        return false;
    }

    @Override
    public List<ProviderValidationMessage> validate(Map<String, Object> options) {
        // TODO validate
        return Collections.emptyList();
    }

}
