#pragma once

#include "ReadXmlExpression.h"
#include "ParserModel.h"
#include "ElementExpression.h"
#include "ElementsExpression.h"
#include "PropertyTerminalExpression.h"
#include "PropertysTerminalExpression.h"
#include "ElementTerminalExpression.h"
#include "ElementsTerminalExpression.h"
#include <string>
#include <map>
#include <vector>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace interpreter
			{
				namespace example5
				{

					///
					/// <summary> * �����﷨���������ʽ��ת����Ϊ��Ӧ�ĳ����﷨�� </summary>
					/// 
					class Parser
					{
					///	
					///	 <summary> * ˽�л��������������ⲿ��ν�Ĵ�������ʵ�� </summary>
					///	 
					private:
						Parser();
						//���弸���������ڲ�ʹ��
//JAVA TO C++ CONVERTER TODO TASK: Native C++ only allows initialization of static const integral fields in their declarations:
						static const std::string BACKLASH = "/";
//JAVA TO C++ CONVERTER TODO TASK: Native C++ only allows initialization of static const integral fields in their declarations:
						static const std::string DOT = ".";
//JAVA TO C++ CONVERTER TODO TASK: Native C++ only allows initialization of static const integral fields in their declarations:
						static const std::string DOLLAR = "$";
					///	
					///	 <summary> * ���շֽ���Ⱥ��¼��Ҫ������Ԫ�ص����� </summary>
					///	 
						static std::vector<std::string> listEle;
					///	
					///	 <summary> * ����һ���ַ������ʽ��ͨ����������ϳ�Ϊһ��������﷨�� </summary>
					///	 * <param name="expr"> ����Ҫȡֵ���ַ������ʽ </param>
					///	 * <returns> ��Ӧ�ĳ����﷨�� </returns>
					///	 
					public:
						static ReadXmlExpression *parse(std::string expr);


					/*----------------------��ʼʵ�ֵ�һ��-----------------------*/			
					///	
					///	 <summary> * ���մ�����˳�����ֽ���ʽ���õ���Ҫ������Ԫ�����ƣ�
					///	 * ���и�Ԫ�ض�Ӧ�Ľ���ģ�� </summary>
					///	 * <param name="expr"> ��Ҫ�ֽ�ı��ʽ </param>
					///	 * <returns> �õ���Ҫ������Ԫ�����ƣ����и�Ԫ�ض�Ӧ�Ľ���ģ�� </returns>
					///	 
					private:
						static Map<std::string, ParserModel*> *parseMapPath(std::string expr);
					///	
					///	 <summary> * ���շֽ������λ�ú�������������Ҫ������Ԫ�����ƣ�
					///	 * ���и�Ԫ�ض�Ӧ�Ľ���ģ�� </summary>
					///	 * <param name="end"> �Ƿ������һ�� </param>
					///	 * <param name="ele"> Ԫ������ </param>
					///	 * <param name="propertyValue"> �Ƿ���ȡ���� </param>
					///	 * <param name="mapPath"> ������Ҫ������Ԫ�����ƣ����и�Ԫ�ض�Ӧ�Ľ���ģ�͵�Map���� </param>
					///	 
						static void setParsePath(bool end, std::string ele, bool propertyValue, Map<std::string, ParserModel*> *mapPath);
					/*----------------------��һ��ʵ�ֽ���-----------------------*/

					/*----------------------��ʼʵ�ֵڶ���-----------------------*/	
					///	
					///	 <summary> * �ѷֽ������Ԫ�����ƣ����ݶ�Ӧ�Ľ���ģ��ת����Ϊ��Ӧ�Ľ��������� </summary>
					///	 * <param name="mapPath"> �ֽ��������Ҫ������Ԫ�����ƣ����и�Ԫ�ض�Ӧ�Ľ���ģ�� </param>
					///	 * <returns> ��ÿ��Ԫ��ת����Ϊ��Ӧ�Ľ����������ļ��� </returns>
					///	 
						static std::vector<ReadXmlExpression*> mapPath2Interpreter(Map<std::string, ParserModel*> *mapPath);
					/*----------------------�ڶ���ʵ�ֽ���-----------------------*/	

					/*----------------------��ʼʵ�ֵ�����-----------------------*/		
						static ReadXmlExpression *buildTree(std::vector<ReadXmlExpression*> &list);
					/*----------------------������ʵ�ֽ���-----------------------*/		
					};

				}
			}
		}
	}
}