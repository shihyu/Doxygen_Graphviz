#pragma once

#include "Prototype.h"
#include <string>
#include <map>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace prototype
			{
				namespace example8
				{
					///
					/// <summary> * ԭ�͹����� </summary>
					/// 
					class PrototypeManager
					{
					///	
					///	 <summary> * ������¼ԭ�͵ı�ź�ԭ��ʵ���Ķ�Ӧ��ϵ </summary>
					///	 
					private:
//JAVA TO C++ CONVERTER TODO TASK: Native C++ only allows initialization of static const integral fields in their declarations:
						static Map<std::string, Prototype*> *map = std::map<std::string, Prototype*>();
					///	
					///	 <summary> * ˽�л����췽���������ⲿ��ν�Ĵ���ʵ�� </summary>
					///	 
						PrototypeManager();
					///	
					///	 <summary> * ��ԭ�͹�����������ӻ����޸�ĳ��ԭ��ע�� </summary>
					///	 * <param name="prototypeId"> ԭ�ͱ�� </param>
					///	 * <param name="prototype"> ԭ��ʵ�� </param>
					///	 
					public:
						static void setPrototype(std::string prototypeId, Prototype *prototype);
					///	
					///	 <summary> * ��ԭ�͹���������ɾ��ĳ��ԭ��ע�� </summary>
					///	 * <param name="prototypeId"> ԭ�ͱ�� </param>
					///	 
						static void removePrototype(std::string prototypeId);
					///	
					///	 <summary> * ��ȡĳ��ԭ�ͱ�Ŷ�Ӧ��ԭ��ʵ�� </summary>
					///	 * <param name="prototypeId"> ԭ�ͱ�� </param>
					///	 * <returns> ԭ�ͱ�Ŷ�Ӧ��ԭ��ʵ�� </returns>
					///	 * <exception cref="Exception"> ���ԭ�ͱ�Ŷ�Ӧ��ԭ��ʵ�������ڣ��������� </exception>
					///	 
						static Prototype *getPrototype(std::string prototypeId) throw(Exception);

					};

				}
			}
		}
	}
}