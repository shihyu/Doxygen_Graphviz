#pragma once

#include "XmlUtil.h"
#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace interpreter
			{
				namespace example3
				{


					using org::w3c::dom::Document;
					using org::w3c::dom::Element;
					using org::w3c::dom::NodeList;
					///
					/// <summary> *	�����ģ�����������������Ҫ��һЩȫ����Ϣ </summary>
					/// 
					class Context
					{
					///	
					///	 <summary> * ��һ���������Ԫ�� </summary>
					///	 
					private:
						Element *preEle;
					///	
					///	 <summary> * Dom����Xml��Document���� </summary>
					///	 
						Document *document;
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
					///	 * ���ݸ�Ԫ�غ͵�ǰԪ�ص���������ȡ��ǰ��Ԫ�� </summary>
					///	 * <param name="pEle"> ��Ԫ�� </param>
					///	 * <param name="eleName"> ��ǰԪ�ص����� </param>
					///	 * <returns> �ҵ��ĵ�ǰԪ�� </returns>
					///	 
						virtual Element *getNowEle(Element *pEle, std::string eleName);

						virtual Element *getPreEle();
						virtual void setPreEle(Element *preEle);

						virtual Document *getDocument();
					};

				}
			}
		}
	}
}