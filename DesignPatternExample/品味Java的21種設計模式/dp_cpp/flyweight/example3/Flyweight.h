#pragma once

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace flyweight
			{
				namespace example3
				{
					///<summary> *
					/// * ������Ȩ���ݵ���Ԫ�ӿ� </summary>
					/// 
					class Flyweight
					{
					///	
					///	 <summary> * �жϴ���İ�ȫʵ���Ȩ�ޣ��Ƿ����Ԫ�����ڲ�״̬ƥ�� </summary>
					///	 * <param name="securityEntity"> ��ȫʵ�� </param>
					///	 * <param name="permit"> Ȩ�� </param>
					///	 * <returns> true��ʾƥ�䣬false��ʾ��ƥ�� </returns>
					///	 
					public:
						virtual public bool match(std::string, std::string) = 0;
					};
				}
			}
		}
	}
}