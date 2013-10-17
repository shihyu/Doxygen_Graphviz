#pragma once

#include "Flyweight.h"
#include <string>
#include <vector>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace flyweight
			{
				namespace example5
				{


					///
					/// <summary> * ����Ҫ�������Ԫ�����ʵ�֣�Ҳ�����ģʽ�е���϶��� </summary>
					/// 
					class UnsharedConcreteFlyweight : public Flyweight
					{
					///	
					///	 <summary> * ��¼ÿ����϶���������������� </summary>
					///	 
					private:
						std::vector<Flyweight*> list;

					public:
						virtual void add(Flyweight *f);

						virtual bool match(std::string securityEntity, std::string permit);

					private:
						bool initialized;
						void InitializeInstanceFields()
						{
							if ( ! initialized)
							{
								list = std::vector<Flyweight*>();

								initialized = true;
							}
						}

public:
	UnsharedConcreteFlyweight()
	{
		InitializeInstanceFields();
	}
					};

				}
			}
		}
	}
}