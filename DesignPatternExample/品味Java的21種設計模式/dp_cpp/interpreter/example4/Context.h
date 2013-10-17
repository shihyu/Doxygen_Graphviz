#pragma once

#include "XmlUtil.h"
#include <string>
#include <vector>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace interpreter
			{
				namespace example4
				{


					using javax::xml::parsers::DocumentBuilder;
					using javax::xml::parsers::DocumentBuilderFactory;

					using org::w3c::dom::Document;
					using org::w3c::dom::Element;
					using org::w3c::dom::NodeList;

					///
					/// <summary> *	�����ģ�����������������Ҫ��һЩȫ����Ϣ </summary>
					/// 
					class Context
					{
					///	
					///	 <summary> * Dom����Xml��Document���� </summary>
					///	 
					private:
						Document *document;
					///	
					///	 <summary> * ��һ�α�����Ķ��Ԫ�� </summary>
					///	 
						std::vector<Element*> preEles;
					///	
					///	 <summary> * ���췽�� </summary>
					///	 * <param name="filePathName"> ��Ҫ��ȡ��xml��·�������� </param>
					///	 * <exception cref="Exception"> </exception>
					///	 
					public:
						Context(std::string filePathName) throw(Exception);
					///	
					///	 <summary> * ���³�ʼ�������� </summary>
					///	 
						virtual void reInit();
					///	
					///	 <summary> * ����Expression����ʹ�õķ���,
					///	 * ���ݸ�Ԫ�غ͵�ǰԪ�ص���������ȡ��ǰ�Ķ��Ԫ�صļ��� </summary>
					///	 * <param name="pEle"> ��Ԫ��  </param>
					///	 * <param name="eleName"> ��ǰԪ�ص����� </param>
					///	 * <returns> ��ǰ�Ķ��Ԫ�صļ��� </returns>
					///	 
						virtual std::vector<Element*> getNowEles(Element *pEle, std::string eleName);

						virtual Document *getDocument();
						virtual std::vector<Element*> getPreEles();
						virtual void setPreEles(std::vector<Element*> &nowEles);

					private:
						bool initialized;
						void InitializeInstanceFields()
						{
							if ( ! initialized)
							{
								preEles = std::vector<Element*>();

								initialized = true;
							}
						}
					};

				}
			}
		}
	}
}