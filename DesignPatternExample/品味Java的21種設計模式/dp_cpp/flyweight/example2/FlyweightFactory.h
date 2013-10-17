#pragma once

#include "Flyweight.h"
#include "ConcreteFlyweight.h"
#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace flyweight
			{
				namespace example2
				{

					///
					/// <summary> * ��Ԫ���� </summary>
					/// 
					class FlyweightFactory
					{
					///	
					///	 <summary> * ������flyweight��������ֻ��ʾ��һ�� </summary>
					///	 
					private:
						Map<std::string, Flyweight*> *fsMap;
					///	
					///	 <summary> * ��ȡkey��Ӧ����Ԫ���� </summary>
					///	 * <param name="key"> ��ȡ��Ԫ�����key��ֻ��ʾ�� </param>
					///	 * <returns> key��Ӧ����Ԫ���� </returns>
					///	 
					public:
						virtual Flyweight *getFlyweight(std::string key);

					private:
						bool initialized;
						void InitializeInstanceFields()
						{
							if ( ! initialized)
							{
								fsMap = std::map<std::string, Flyweight*>();

								initialized = true;
							}
						}

public:
	FlyweightFactory()
	{
		InitializeInstanceFields();
	}
					};
				}
			}
		}
	}
}