#pragma once

#include "Component.h"
#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace composite
			{
				namespace example6
				{
					///
					/// <summary> * Ҷ�Ӷ��� </summary>
					/// 
					class Leaf : public Component
					{
					///	
					///	 <summary> * Ҷ�Ӷ�������� </summary>
					///	 
					private:
						std::string name;
					///	
					///	 <summary> * ���췽��������Ҷ�Ӷ�������� </summary>
					///	 * <param name="name"> Ҷ�Ӷ�������� </param>
					///	 
					public:
						Leaf(std::string name);
					///	
					///	 <summary> * ���Ҷ�Ӷ���Ľṹ��Ҷ�Ӷ���û���Ӷ���Ҳ�������Ҷ�Ӷ�������� </summary>
					///	 * <param name="preStr"> ǰ׺����Ҫ�ǰ��ղ㼶ƴ�ӵĿո�ʵ��������� </param>
					///	 
						virtual void printStruct(std::string preStr);

					private:
						bool initialized;
						void InitializeInstanceFields()
						{
							if ( ! initialized)
							{
								name = "";

								initialized = true;
							}
						}
					};

				}
			}
		}
	}
}